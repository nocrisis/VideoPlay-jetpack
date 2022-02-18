package com.catherine.videoplay.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.catherine.libnetwork.ApiResponse;
import com.catherine.libnetwork.ApiService;
import com.catherine.libnetwork.JsonCallback;
import com.catherine.videoplay.R;
import com.catherine.videoplay.model.User;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private View actionClose, actionLogin;
    private Tencent tencent;
    private IUiListener loginListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_login);
        actionClose = findViewById(R.id.action_close);
        actionLogin = findViewById(R.id.action_login);
        Tencent.setIsPermissionGranted(true);
        actionClose.setOnClickListener(this);
        actionLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int clickId = v.getId();
        switch (clickId) {
            case R.id.action_close:
                finish();
                break;
            case R.id.action_login:
                login();
                break;
        }
    }

    private void login() {
        if (tencent == null) {
            tencent = Tencent.createInstance("101992947", getApplicationContext());
        }
        loginListener = new IUiListener() {
            @Override
            public void onComplete(Object o) {
                Log.d("LoginQQRes",o.toString());
                JSONObject response = (JSONObject) o;
                try {
                    String openid = response.getString("openid");
                    String access_token = response.getString("access_token");
                    String expires_in = response.getString("expires_in");
                    long expires_time = response.getLong("expires_time");
                    tencent.setAccessToken(access_token, expires_in);
                    tencent.setOpenId(openid);
                    QQToken qqToken = tencent.getQQToken();
                    getUserInfo(qqToken, expires_time, openid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(UiError uiError) {
                Toast.makeText(getApplicationContext(), "登录失败：reason：" + uiError.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "登录取消", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onWarning(int i) {
                Toast.makeText(getApplicationContext(), "登录警告i=" + i, Toast.LENGTH_SHORT).show();
            }
        };
        tencent.login(this, "all", loginListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("LoginActivity", "-->onActivityResult " + requestCode + " resultCode=" + resultCode);
        if (requestCode == Constants.REQUEST_LOGIN ||
                requestCode == Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode, resultCode, data, loginListener);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getUserInfo(QQToken qqToken, long expires_time, String openid) {
        UserInfo userInfo = new UserInfo(getApplicationContext(), qqToken);
        userInfo.getUserInfo(new IUiListener() {
            @Override
            public void onComplete(Object o) {
                JSONObject response = (JSONObject) o;
                try {
                    String nickname = response.getString("nickname");
                    String figure_url2 = response.getString("figureurl_2");
                    save(nickname, figure_url2, openid, expires_time);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(UiError uiError) {
                Toast.makeText(getApplicationContext(), "登录失败：reason：" + uiError.toString(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "登录取消", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onWarning(int i) {

            }
        });
    }

    private void save(String nickname, String avatar, String openid, long expires_time) {
        ApiService.get("/user/insert")
                .addParam("name", nickname)
                .addParam("avatar", avatar)
                .addParam("qqOpenId", openid)
                .addParam("expires_time", expires_time)
                .execute(new JsonCallback<User>() {
                    @Override
                    public void onSuccess(ApiResponse<User> response) {
                        if (response.data != null) {
                            UserManager.get().save(response.data);
                        } else {
                            //此时在子线程
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this, "登录失败，msg" + response.message, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(ApiResponse<User> response) {
                        super.onError(response);
                    }

                    @Override
                    public void onCacheSuccess(ApiResponse<User> response) {
                        super.onCacheSuccess(response);
                    }
                });
    }
}
