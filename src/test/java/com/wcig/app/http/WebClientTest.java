package com.wcig.app.http;

import com.wcig.app.App;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= App.class)
public class WebClientTest {

    @Resource
    private WebClientUtil webClientUtil;

    @Test
    public void testFirst() {
        String url = "http://localhost:28080/get";
        WebClient client = WebClient.create();

        String result1 = client.get().uri(url).retrieve().bodyToMono(String.class).block();
        log.info("response result1: {}", result1);

        Result result2 = client.get().uri(url).retrieve().bodyToMono(Result.class).block();
        log.info("response result2: {}", result2);
        assertNotNull(result2);
    }

    @Test
    public void testGet() {
        String url = "http://localhost:28080/get";
        Map<String, Object> urlParams = new HashMap<>();
        urlParams.put("id", 100);
        urlParams.put("name", "tom");

        Result result = webClientUtil.get(url, urlParams, Result.class);
        log.info("get response result: {}", result);
        assertNotNull(result);
    }

    @Test
    public void testPostJson() {
        String url = "http://localhost:28080/postJson";
        User user = new User().setId(100).setName("tom");

        Result result = webClientUtil.postJson(url, new HashMap<>(), user, Result.class);
        log.info("post json response result: {}", result);
        assertNotNull(result);
    }

    @Test
    public void testPostFormUrlencoded() {
        String url = "http://localhost:28080/postFormUrlencoded";
        Map<String, Object> params = new HashMap<>();
        params.put("id", 100);
        params.put("name", "tom");

        Result result = webClientUtil.postFormUrlencoded(url, params, Result.class);
        log.info("post form urlencoded response result: {}", result);
        assertNotNull(result);
    }

    @Test
    public void testPostFormData() {
        String url = "http://localhost:28080/postFormData";
        Map<String, Object> params = new HashMap<>();
        params.put("id", 100);
        params.put("name", "tom");
        String filePath = "/Users/yangbo/Pictures/test.png";
        Map<String, File> files = new HashMap<>();
        files.put("file", new File(filePath));

        Result result = webClientUtil.postFormData(url, params, files, Result.class);
        log.info("response result: {}", result);
        assertNotNull(result);
    }
}
