package com.example.demo.application.security;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Slf4j
@Configuration
public class Resttem {
    @Bean
    public RestTemplate restTemplate(List<HttpMessageConverter<?>> messageConverters) {
        RestTemplate restTemplate = new RestTemplateBuilder()
                .interceptors((request, body, execution) -> {
                    log.debug("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"+request.toString()); // 요청 정보 출력
                    log.debug(request.getHeaders().toString());
                    log.debug(body.toString());
                    ClientHttpResponse response = execution.execute(request, body);
                    log.debug(response.toString()); // 응답 정보 출력
                    log.debug(response.getHeaders().toString());
                    log.debug(response.getBody().toString());
                    return response;
                }).additionalMessageConverters(messageConverters)
                .build();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        return restTemplate;
    }
}
