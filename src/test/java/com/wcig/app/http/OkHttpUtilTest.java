package com.wcig.app.http;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * 测试下来okhttp3不大好用..
 */
@Slf4j
public class OkHttpUtilTest {

    @Test
    public void testGet() throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://localhost:28080/get?id=100&name=tom")
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
        Result result = JSON.parseObject(response.body().string(), Result.class);
        log.info("result: {}", result);
    }

    @Test
    public void testPostJson() throws IOException {
        User user = new User().setId(100).setName("tom");
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(JSON.toJSONString(user), mediaType);
        Request request = new Request.Builder()
                .url("http://localhost:28080/postJson")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        Result result = JSON.parseObject(response.body().string(), Result.class);
        log.info("result: {}", result);
    }

    @Test
    public void testPostFormUrlencoded() throws IOException {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create("id=100&name=tom", mediaType);
        Request request = new Request.Builder()
                .url("http://localhost:28080/postFormUrlencoded")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response = client.newCall(request).execute();
        Result result = JSON.parseObject(response.body().string(), Result.class);
        log.info("result: {}", result);
    }

    @Test
    public void testPostFormData() throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("id", "100")
                .addFormDataPart("name", "tom")
                .addFormDataPart("file", "test.png",
                        RequestBody.create(new File("/Users/yangbo/Pictures/test.png"),
                                MediaType.parse("application/octet-stream")))
                .build();
        Request request = new Request.Builder()
                .url("http://localhost:28080/postFormData")
                .method("POST", body)
                .build();
        Response response = client.newCall(request).execute();
        Result result = JSON.parseObject(response.body().string(), Result.class);
        log.info("result: {}", result);
    }
}