package com.easymap.easymap.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.*;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.stream.Collectors;

@Configuration
public class RestTemplateConfig {

    @Bean
    RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofSeconds(5))
                .setReadTimeout(Duration.ofSeconds(5))
                .additionalInterceptors(new LoggingInterceptor())
                .requestFactory(() -> new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
                .build();

    }
    @Slf4j
    static class LoggingInterceptor implements ClientHttpRequestInterceptor {

        @Override
        public ClientHttpResponse intercept(HttpRequest req, byte[] body, ClientHttpRequestExecution ex) throws IOException {
            final String sessionNumber = makeSessionNumber();
            printRequest(sessionNumber, req, body);
            ClientHttpResponse response = ex.execute(req, body);
            printResponse(sessionNumber, response);
            return response;
        }

        private String makeSessionNumber() {
            return Integer.toString((int) (Math.random() * 1000000));
        }

        private void printRequest(final String sessionNumber, final HttpRequest req, final byte[] body) {
            log.info("[{}] URI: {}, Method: {}, Headers:{}, Body:{} ",
                    sessionNumber, req.getURI(), req.getMethod(), req.getHeaders(), new String(body, StandardCharsets.UTF_8));
        }

        private void printResponse(final String sessionNumber, final ClientHttpResponse res) throws IOException {
            String body = new BufferedReader(new InputStreamReader(res.getBody(), StandardCharsets.UTF_8)).lines()
                    .collect(Collectors.joining("\n"));

            log.info("[{}] Status: {}, Headers:{}, Body:{} ",
                    sessionNumber, res.getStatusCode(), res.getHeaders(), body);
        }
    }

}
