package com.catherine.videoplay.model;

public class Destination {
    private int id;
    private String pageUrl;
    private String className;
    private boolean needLogin;
    private boolean asStarter;
    private boolean isFragment;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public boolean isNeedLogin() {
        return needLogin;
    }

    public void setNeedLogin(boolean needLogin) {
        this.needLogin = needLogin;
    }

    public boolean isAsStarter() {
        return asStarter;
    }

    public void setAsStarter(boolean asStarter) {
        this.asStarter = asStarter;
    }

    public boolean isFragment() {
        return isFragment;
    }

    public void setFragment(boolean fragment) {
        isFragment = fragment;
    }
}
