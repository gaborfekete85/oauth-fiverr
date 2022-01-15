package com.aks.gateway.controller;

import com.aks.gateway.filter.domain.AccessTokenResponse;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RefreshController {

    @GetMapping("/refresh")
    public AccessTokenResponse refresh(@RegisteredOAuth2AuthorizedClient("okta") OAuth2AuthorizedClient client) {
        return new AccessTokenResponse(client.getAccessToken().getTokenValue(), client.getRefreshToken().getTokenValue());
    }
}
