package com.catherine.videoplay;

import android.app.Application;

import com.catherine.libnetwork.ApiService;

public class VideoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ApiService.init(Constants.ip + "mvvmserver", null);
    }
}
