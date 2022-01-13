package com.aks.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableWebFluxSecurity
public class OktaOAuth2WebSecurity {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain2(ServerHttpSecurity http)
    {
        http
                .csrf().disable()
                .authorizeExchange().pathMatchers("/greeting").hasAuthority("SCOPE_email")
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
}