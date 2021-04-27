package com.wcig.app.http;

import com.wcig.app.App;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=App.class)
public class RestTemplateTest {

    @Resource
    private RestTemplate restTemplate;
    @Resource
    private RestTemplateUtil restTemplateUtil;

    @Test
    public void testGet() {
        String url1 = "http://localhost:28080/get?id=100&name=tom";
        String result1 = restTemplateUtil.get(url1);
        log.info("response result1: {}", result1);

        String url2 = "http://localhost:28080/get";
        Map<String, Object> params = new HashMap<>();
        params.put("id", 100);
        params.put("name", "tom");
        String result2 = restTemplateUtil.get(url2, params);
        log.info("response result2: {}", result2);

        Result result3 = restTemplateUtil.get(url2, params, Result.class);
        log.info("response result3: {}", result3);
    }

    @Test
    public void testPostJson() {
        String url = "http://localhost:28080/postJson";
        User user = new User().setId(100).setName("tom");

        String result1 = restTemplateUtil.postJson(url, user);
        log.info("response result1: {}", result1);

        Result result2 = restTemplateUtil.postJson(url, user, Result.class);
        log.info("response result2: {}", result2);

        String result3 = restTemplateUtil.postJson(url, new HashMap<>(), user);
        log.info("response result3: {}", result3);

        Result result4 = restTemplateUtil.postJson(url, new HashMap<>(), user, Result.class);
        log.info("response result4: {}", result4);
    }

    @Test
    public void testPostFormUrlencoded() {
        String url = "http://localhost:28080/postFormUrlencoded";
        Map<String, Object> params = new HashMap<>();
        params.put("id", 100);
        params.put("name", "tom");

        String result1 = restTemplateUtil.postFormUrlencoded(url, params);
        log.info("response result1: {}", result1);

        Result result2 = restTemplateUtil.postFormUrlencoded(url, params, Result.class);
        log.info("response result2: {}", result2);
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

        String result1 = restTemplateUtil.postFormData(url, params);
        log.info("response result1: {}", result1);

        String result2 = restTemplateUtil.postFormData(url, params, files);
        log.info("response result2: {}", result2);

        Result result3 = restTemplateUtil.postFormData(url, params, Result.class);
        log.info("response result3: {}", result3);

        Result result4 = restTemplateUtil.postFormData(url, params, files, Result.class);
        log.info("response result4: {}", result4);
    }

    @Test
    public void testHeader() {
        String url = "http://localhost:28080/head";
        HttpHeaders httpHeaders = restTemplate.headForHeaders(url);
        log.info("response headers: {}", httpHeaders);
    }

    @Test
    public void testOption() {
        String url = "http://localhost:28080/options";
        Set<HttpMethod> optionsForAllow = restTemplate.optionsForAllow(url);
        log.info("response options: {}", optionsForAllow);
        // HttpMethod[] supportedMethods = {HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE};
    }
}
