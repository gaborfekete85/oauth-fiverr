package com.aks.gateway.config;

import com.okta.jwt.AccessTokenVerifier;
import com.okta.jwt.JwtVerifiers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
@EnableWebFluxSecurity
public class OktaOAuth2WebSecurity {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain2(ServerHttpSecurity http)
    {
        http
                .csrf().disable()
                .authorizeExchange()
                    .pathMatchers("/refresh", "/actuator/**").permitAll()
                    .pathMatchers("/greeting").hasAuthority("SCOPE_email")
                .anyExchange()
                .authenticated()
                .and().oauth2Login()
                .and().oauth2Client()
                .and().oauth2ResourceServer().jwt();
        return http.build();
    }

    @Bean
    RestTemplate createRestTemplate() {
        return new RestTemplate();
    }


    @Bean
    public AccessTokenVerifier createVerifier() {
        AccessTokenVerifier jwtVerifier = JwtVerifiers.accessTokenVerifierBuilder()
                .setIssuer(issuerUri)
                .setAudience("api://default")                // defaults to 'api://default'
                .setConnectionTimeout(Duration.ofSeconds(1))// defaults to 1s
                .build();
        return jwtVerifier;
    }

}