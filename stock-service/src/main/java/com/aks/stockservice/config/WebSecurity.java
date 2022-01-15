package com.aks.stockservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;

@Configuration
public class WebSecurity extends WebSecurityConfigurerAdapter {

    protected void configure(HttpSecurity http) throws Exception {
        http
         .authorizeRequests(authorize -> authorize
                  .mvcMatchers("/order/**").hasAuthority("SCOPE_email")
                  .anyRequest().authenticated()
          )
          .oauth2Login().and()
          .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
    }
}
