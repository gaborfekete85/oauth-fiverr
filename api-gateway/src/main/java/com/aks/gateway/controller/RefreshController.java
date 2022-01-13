package com.aks.gateway.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ServerWebExchange;

@Controller
public class RefreshController {

    @RequestMapping("/refresh")
    public String refresh(ServerWebExchange exchange,
                           @AuthenticationPrincipal OidcUser oidcUser, Model model,
                           @RegisteredOAuth2AuthorizedClient("okta") OAuth2AuthorizedClient client) {
        model.addAttribute("username", oidcUser.getEmail());
        model.addAttribute("idToken", oidcUser.getIdToken());
        model.addAttribute("accessToken", client.getAccessToken());
        model.addAttribute("refreshToken", client.getRefreshToken() != null ? client.getRefreshToken().getTokenValue() : "No Refresh Token Provided.");
        model.addAttribute("sessionId", exchange.getRequest().getCookies().get("SESSION").get(0).toString().substring(8));
        return "greeting";
    }

    @RequestMapping("/greeting")
    public String greeting(ServerWebExchange exchange,
                           @AuthenticationPrincipal OidcUser oidcUser, Model model,
                           @RegisteredOAuth2AuthorizedClient("okta") OAuth2AuthorizedClient client) {
        model.addAttribute("username", oidcUser.getEmail());
        model.addAttribute("idToken", oidcUser.getIdToken());
        model.addAttribute("accessToken", client.getAccessToken());
        model.addAttribute("refreshToken", client.getRefreshToken() != null ? client.getRefreshToken().getTokenValue() : "No Refresh Token Provided.");
        model.addAttribute("sessionId", exchange.getRequest().getCookies().get("SESSION").get(0).toString().substring(8));
        return "greeting";
    }
}