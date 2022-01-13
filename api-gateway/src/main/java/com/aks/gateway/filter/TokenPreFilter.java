package com.aks.gateway.filter;

import com.aks.gateway.filter.domain.TokenValidationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Component
@Order(1000)
public class TokenPreFilter implements GlobalFilter {

    final Logger logger = LoggerFactory.getLogger(TokenPreFilter.class);

    @Value("${spring.security.oauth2.client.registration.okta-web.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.okta-web.client-secret}")
    private String clientSecret;

    @Value("${app.introspectUrl}")
    private String introspectUrl;

    @Autowired
    RestTemplate restTemplate;

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain) {
        processRequest(exchange);
        return chain.filter(exchange);
    }

    public void processRequest(ServerWebExchange exchange) {
        String token = exchange.getRequest().getHeaders().get("Authorization").get(0).toString();
        logger.info("Populating access token: " + token);
        if(!isTokenValid(token)) {
            logger.error("Token needs to be updated ! Request a new Access token !");
            refreshToken(exchange.getRequest().getCookies().get("SESSION").get(0).getValue());
        } else {
            logger.info("Token is active !");
        }
    }

    private boolean isTokenValid(String jwtToken) {
        // create headers
        HttpHeaders headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String basicAuthHeader = Base64.getEncoder().encodeToString(String.format("%s:%s", clientId, clientSecret).getBytes(StandardCharsets.UTF_8));
        headers.set("Authorization", "Basic " + basicAuthHeader);
        // build the request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(null, headers);
        String url = String.format(introspectUrl, jwtToken.substring(7));
        ResponseEntity<TokenValidationResponse> response = restTemplate.postForEntity(url, entity, TokenValidationResponse.class);
        return response.getBody().isActive();
    }

    public void refreshToken(String sessionId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "SESSION=" + sessionId);
        headers.setAccept(List.of(MediaType.TEXT_HTML));
        String url = "http://localhost:8080/refresh";
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        if(response.getStatusCode() == HttpStatus.OK) {
            logger.info("Token refreshed");
        } else {
            logger.error("Token refresh FAILED");
        }
    }
}