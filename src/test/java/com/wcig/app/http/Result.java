package com.wcig.app.http;

import lombok.Data;

@Data
public class Result {
    private int code;
    private Object data;
    private String msg;
}
