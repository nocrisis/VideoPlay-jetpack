package com.catherine.videoplay.ui.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.catherine.libnetwork.ApiResponse;
import com.catherine.libnetwork.ApiService;
import com.catherine.libnetwork.JsonCallback;
import com.catherine.videoplay.R;
import com.catherine.videoplay.model.User;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private View actionClose, actionLogin;
    private Tencent tencent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_login);
        actionClose = findViewById(R.id.action_close);
        actionLogin = findViewById(R.id.action_login);
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
            tencent = Tencent.createInstance("101991967", getApplicationContext());
        }
        tencent.login(
                this, "all", new IUiListener() {
                    @Override
                    public void onComplete(Object o) {
                        JSONObject response = (JSONObject) o;
                        try {
                            String openid = response.getString("openid");
                            String access_token = response.getString("access_token");
                            String expire_in = response.getString("expire_in");
                            long expires_time = response.getLong("expire_time");
                            tencent.setAccessToken(access_token, expire_in);
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

                    }
                }
        );
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
                .execute(new JsonCallback<User>(){
                    @Override
                    public void onSuccess(ApiResponse<User> response) {
                        if (response.body != null) {
                            UserManager.get().save(response.body);
                        }else{
                            //此时在子线程
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this, "登录失败，msg"+response.message, Toast.LENGTH_SHORT).show();
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
