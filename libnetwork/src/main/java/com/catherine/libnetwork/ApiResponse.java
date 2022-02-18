package com.catherine.libnetwork;

import com.alibaba.fastjson.annotation.JSONField;

public class ApiResponse<T> {
    public String message;
    public boolean success;
    public int status;
    public T data;

}
