package com.wcig.app.http;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.io.IOException;

@Slf4j
public class OkHttpUtilTest {

    @Test
    public void test() throws IOException, InterruptedException {
        log.info("okhttp");
        String url = "http://localhost:28081/get";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        if (response.code() != HttpStatus.OK.value()) {
            log.error("response code not 200");
            return;
        }

        // 同步
        Result result = JSON.parseObject(response.body().string(), Result.class);
        log.info("response result: {}", result);

        // 异步
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                log.error("response code not 200: {}", e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                log.info("response: {}", response);
            }
        });

        Thread.sleep(1000);
    }
}