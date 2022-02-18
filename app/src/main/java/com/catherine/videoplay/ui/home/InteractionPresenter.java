package com.catherine.videoplay.ui.home;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.alibaba.fastjson.JSONObject;
import com.catherine.libcommon.AppGlobals;
import com.catherine.libnetwork.ApiResponse;
import com.catherine.libnetwork.ApiService;
import com.catherine.libnetwork.JsonCallback;
import com.catherine.videoplay.model.Feed;
import com.catherine.videoplay.model.User;
import com.catherine.videoplay.ui.login.UserManager;

public class InteractionPresenter {
    public static final String URL_TOGGLE_FEED_LIKE = "/ugc/toggleFeedLike";
    public static final String URL_TOGGLE_FEED_DISS = "/ugc/dissFeed";

    public static void toggleFeedLike(LifecycleOwner owner,Feed feed) {
        if (!UserManager.get().isLogin()) {
            LiveData<User> loginLiveData = UserManager.get().login(AppGlobals.getApplication());
            loginLiveData.observe(owner, new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    if (user != null) {
                        toggleFeedLikeInternal(feed);
                    }
                    loginLiveData.removeObserver(this);
                }
            });
            return;
        }
        toggleFeedLikeInternal(feed);
    }

    public static void toggleFeedLikeInternal(Feed feed) {
        ApiService.get(URL_TOGGLE_FEED_LIKE)
                .addParam("userId", UserManager.get().getUserId())
                .addParam("itemId", feed.getItemId())
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(ApiResponse<JSONObject> response) {
                        if (response.data != null) {
                            boolean hasLiked = response.data.getBoolean("hasLiked");
                            feed.getUgc().setHasLiked(hasLiked);
                        }
                    }
                });
    }

    public static void toggleFeedDiss(LifecycleOwner owner,Feed feed) {
        if (!UserManager.get().isLogin()) {
            LiveData<User> loginLiveData = UserManager.get().login(AppGlobals.getApplication());
            loginLiveData.observe(owner, new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    if (user != null) {
                        toggleFeedDissInternal(feed);
                    }
                    loginLiveData.removeObserver(this);
                }
            });
            return;
        }
        toggleFeedDissInternal(feed);
    }

    public static void toggleFeedDissInternal(Feed feed) {
        ApiService.get(URL_TOGGLE_FEED_DISS)
                .addParam("userId", UserManager.get().getUserId())
                .addParam("itemId", feed.getItemId())
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(ApiResponse<JSONObject> response) {
                        if (response.data != null) {
                            boolean hasDissed = response.data.getBoolean("hasDissed");
                            feed.getUgc().setHasDissed(hasDissed);
                        }
                    }
                });
    }
}
