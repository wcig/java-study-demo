package com.wcig.app.http;

import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import javax.annotation.Resource;
import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTP请求工具类 (WebClient)
 * <p>
 * 1.特点: 异步非阻塞
 * 2.依赖包: spring-web, reactor-core, reactor-netty, jackson-core, jackson-databind (可选:jackson-annotations)
 * 3.异常: 4xx,5xx抛出RestTemplate定义异常, 1xx,3xx抛出自定义异常RequestException
 */
@Component
public class WebClientUtil {

    @Resource
    private WebClient webClient;

    @Bean
    public WebClient webClient() {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofMillis(30000));
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    public String get(String url) throws RequestException {
        Map<String, Object> urlParams = new HashMap<>();
        return get(url, urlParams);
    }

    public String get(String url, Map<String, Object> urlParams) throws RequestException {
        return get(url, urlParams, String.class);
    }

    public <T> T get(String url, Map<String, Object> urlParams, Class<T> clazz) throws RequestException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        urlParams.forEach(builder::queryParam);

        return webClient.get()
                .uri(url)
                .exchange()
                .flatMap(clientResponse -> {
                    if (!clientResponse.statusCode().is2xxSuccessful()) {
                        return Mono.error(new RequestException("http status not 2xx", url, clientResponse.rawStatusCode(), urlParams, null));
                    }
                    return clientResponse.bodyToMono(clazz);
                }).block();
    }

    public String postJson(String url, Object body) throws RequestException {
        return postJson(url, body, String.class);
    }

    public String postJson(String url, Map<String, Object> urlParams, Object body) throws RequestException {
        return postJson(url, urlParams, body, String.class);
    }

    public <T> T postJson(String url, Object body, Class<T> clazz) throws RequestException {
        Map<String, Object> urlParams = new HashMap<>();
        return postJson(url, urlParams, body, clazz);
    }

    public <T> T postJson(String url, Map<String, Object> urlParams, Object body, Class<T> clazz) throws RequestException {
        return webClient.post().uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(body))
                .exchange()
                .flatMap(clientResponse -> {
                    if (!clientResponse.statusCode().is2xxSuccessful()) {
                        return Mono.error(new RequestException("http status not 2xx", url, clientResponse.rawStatusCode(), urlParams, null));
                    }
                    return clientResponse.bodyToMono(clazz);
                }).block();
    }

    public String postFormUrlencoded(String url, Map<String, Object> params) throws RequestException {
        return postFormUrlencoded(url, params, String.class);
    }

    public <T> T postFormUrlencoded(String url, Map<String, Object> params, Class<T> clazz) throws RequestException {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        params.forEach((k, v) -> map.add(k, String.valueOf(v)));
        return webClient.post().uri(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(map))
                .exchange()
                .flatMap(clientResponse -> {
                    if (!clientResponse.statusCode().is2xxSuccessful()) {
                        return Mono.error(new RequestException("http status not 2xx", url, clientResponse.rawStatusCode(), null, null));
                    }
                    return clientResponse.bodyToMono(clazz);
                }).block();
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
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        params.forEach((k, v) -> map.add(k, String.valueOf(v)));
        files.forEach((k, v) -> map.add(k, new FileSystemResource(v)));
        return webClient.post().uri(url)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(map))
                .exchange()
                .flatMap(clientResponse -> {
                    if (!clientResponse.statusCode().is2xxSuccessful()) {
                        return Mono.error(new RequestException("http status not 2xx", url, clientResponse.rawStatusCode(), null, params));
                    }
                    return clientResponse.bodyToMono(clazz);
                }).block();
    }
}
