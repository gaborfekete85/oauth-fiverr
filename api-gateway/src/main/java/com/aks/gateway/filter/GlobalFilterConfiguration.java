package com.aks.gateway.filter;

import com.aks.gateway.filter.domain.AccessTokenResponse;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyResponseBodyGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Configuration
public class GlobalFilterConfiguration {

    private final String HEADER_ERR_CODE_KEY = "X-ERROR-CODE";
    private final String JWT_EXPIRED_ERROR_CODE = "011";

    @Autowired
    private RestTemplate restTemplate;

    final Logger logger = LoggerFactory.getLogger(GlobalFilterConfiguration.class);

    @Bean
    public GlobalFilter postGlobalFilter2() {
        return (exchange, chain) -> {
            if(isResponseUnauthorized(exchange)) { // TOKEN EXPIRED --> REFRESH
                String token = refreshToken(exchange.getRequest().getCookies().get("SESSION").get(0).getValue());
                logger.debug("New token created: " + token);

                ServerHttpRequest request = exchange.getRequest()
                        .mutate()
                        .header("Authorization", String.format("Bearer %s", token))
                        .build();

                ServerWebExchange exchange1 = exchange.mutate().request(request).build();
                return chain.filter(exchange1);
            }
            return chain.filter(exchange);
        };
    }

    private boolean isResponseUnauthorized(final ServerWebExchange exchange) {
        return exchange.getResponse().getStatusCode() == HttpStatus.UNAUTHORIZED;
    }

    private boolean isTokenExpired(final ServerWebExchange exchange) {
        List<String> header = exchange.getResponse().getHeaders().get(HEADER_ERR_CODE_KEY);
        if(header == null || header.isEmpty()) {
            return false;
        }
        return JWT_EXPIRED_ERROR_CODE.equals(header.get(0));
    }

    public String refreshToken(String sessionId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "SESSION=" + sessionId);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        String url = "http://localhost:8080/refresh";
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<AccessTokenResponse> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, AccessTokenResponse.class);
        if(response.getStatusCode() == HttpStatus.OK) {
            logger.info("Token refreshed");
        } else {
            logger.error("Token refresh FAILED");
        }
        return response.getBody().getAccessToken();
    }
}
