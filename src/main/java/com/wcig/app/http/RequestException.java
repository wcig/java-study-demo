package com.wcig.app.http;

import java.util.Map;

public class RequestException extends RuntimeException {
    private String msg;
    private String url;
    private int statusCode;
    private Map<String, Object> urlParams;
    private Object body;

    public RequestException(String msg, String url, int statusCode, Map<String, Object> urlParams, Object body) {
        this.msg = msg;
        this.url = url;
        this.statusCode = statusCode;
        this.urlParams = urlParams;
        this.body = body;
    }

    @Override
    public String toString() {
        return "RequestException{" +
                "msg='" + msg + '\'' +
                ", url='" + url + '\'' +
                ", statusCode=" + statusCode +
                ", urlParams=" + urlParams +
                ", body=" + body +
                '}';
    }
}
