package com.catherine.videoplay.util;

import android.content.res.AssetManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.catherine.libcommon.AppGlobals;
import com.catherine.videoplay.model.BottomBar;
import com.catherine.videoplay.model.Destination;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class AppConfig {
    private static HashMap<String, Destination> sDestConfig;
    private static BottomBar sBottomBar;
    public static HashMap<String, Destination> getDestConfig() {
        if (sDestConfig == null) {
            String content = parseFile("destination.json");
            sDestConfig = JSON.parseObject(content,
                    new TypeReference<HashMap<String, Destination>>() {}.getType());
        }
        return sDestConfig;
    }

    public static BottomBar getBottomBar() {
        if (sBottomBar == null) {
            String content = parseFile("main_tabs_config.json");
            sBottomBar = JSON.parseObject(content, BottomBar.class);
        }
        return sBottomBar;
    }
    private static String parseFile(String fileName) {
        AssetManager assets =
                AppGlobals.getApplication().getResources().getAssets();
        InputStream is = null;
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            is = assets.open(fileName);
            reader = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {

            }
        }
        return builder.toString();
    }
}
