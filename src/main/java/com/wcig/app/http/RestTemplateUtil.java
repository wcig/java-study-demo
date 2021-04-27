package com.wcig.app.http;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTP请求工具类 (RestTemplate)
 *
 * 1.特点: 同步阻塞
 * 2.依赖包: spring-web, jackson-core, jackson-databind (可选:jackson-annotations)
 * 3.异常: 4xx,5xx抛出RestTemplate定义异常, 1xx,3xx抛出自定义异常RequestException
 */
@Configuration
@Service
public class RestTemplateUtil {

    @Resource
    private RestTemplate restTemplate;

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        return new RestTemplate(factory);
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(30000);
        factory.setConnectTimeout(30000);
        return factory;
    }

    public String get(String url) throws RequestException {
        Map<String, Object> ulrParams = new HashMap<>();
        return get(url, ulrParams);
    }

    public String get(String url, Map<String, Object> ulrParams) throws RequestException {
        return get(url, ulrParams, String.class);
    }

    public <T> T get(String url, Map<String, Object> ulrParams, Class<T> clazz) throws RequestException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        ulrParams.forEach(builder::queryParam);
        ResponseEntity<T> res = restTemplate.getForEntity(builder.toUriString(), clazz);
        if (res.getStatusCode() != HttpStatus.OK) {
            throw new RequestException(url, res.getStatusCodeValue(), ulrParams, null);
        }
        return res.getBody();
    }

    public String postJson(String url, Object body) throws RequestException {
        return postJson(url, body, String.class);
    }

    public String postJson(String url, Map<String, Object> ulrParams, Object body) throws RequestException {
        return postJson(url, ulrParams, body, String.class);
    }

    public <T> T postJson(String url, Object body, Class<T> clazz) throws RequestException {
        Map<String, Object> ulrParams = new HashMap<>();
        return postJson(url, ulrParams, body, clazz);
    }

    public <T> T postJson(String url, Map<String, Object> ulrParams, Object body, Class<T> clazz) throws RequestException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        ulrParams.forEach(builder::queryParam);
        HttpEntity<Object> entity = new HttpEntity<>(body, headers);
        ResponseEntity<T> res = restTemplate.postForEntity(builder.toUriString(), entity, clazz);
        if (res.getStatusCode() != HttpStatus.OK) {
            throw new RequestException(url, res.getStatusCodeValue(), ulrParams, body);
        }
        return res.getBody();
    }

    public String postFormUrlencoded(String url, Map<String, Object> params) throws RequestException {
        return postFormUrlencoded(url, params, String.class);
    }

    public <T> T postFormUrlencoded(String url, Map<String, Object> params, Class<T> clazz) throws RequestException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, Object> map= new LinkedMultiValueMap<>();
        params.forEach((k, v) -> map.add(k, String.valueOf(v)));
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(map, headers);
        ResponseEntity<T> res = restTemplate.postForEntity(url, entity, clazz);
        if (res.getStatusCode() != HttpStatus.OK) {
            throw new RequestException(url, res.getStatusCodeValue(), null, params);
        }
        return res.getBody();
    }

    public String postFormData(String url, Map<String, Object> params) throws RequestException {
        return postFormData(url, params, String.class);
    }

    public String postFormData(String url, Map<String, Object> params, Map<String, File> files) throws RequestException {
        return postFormData(url, params, files, String.class);
    }

    public <T> T postFormData(String url, Map<String, Object> params, Class<T> clazz) throws RequestException {
        Map<String, File> files = new HashMap<>();
        return postFormData(url, params, files, clazz);
    }

    public <T> T postFormData(String url, Map<String, Object> params, Map<String, File> files, Class<T> clazz) throws RequestException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> map= new LinkedMultiValueMap<>();
        params.forEach((k, v) -> map.add(k, String.valueOf(v)));
        files.forEach((k, v) -> map.add(k, new FileSystemResource(v)));
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(map, headers);
        ResponseEntity<T> res = restTemplate.postForEntity(url, entity, clazz);
        if (res.getStatusCode() != HttpStatus.OK) {
            throw new RequestException(url, res.getStatusCodeValue(), null, params);
        }
        return res.getBody();
    }

    public static class RequestException extends RuntimeException {
        private String url;
        private int statusCode;
        private Map<String, Object> ulrParams;
        private Object body;

        public RequestException(String url, int statusCode, Map<String, Object> ulrParams, Object body) {
            this.url = url;
            this.statusCode = statusCode;
            this.ulrParams = ulrParams;
            this.body = body;
        }

        @Override
        public String toString() {
            return "RequestException{" +
                    "url='" + url + '\'' +
                    ", statusCode=" + statusCode +
                    ", ulrParams=" + ulrParams +
                    ", body=" + body +
                    '}';
        }
    }
}
