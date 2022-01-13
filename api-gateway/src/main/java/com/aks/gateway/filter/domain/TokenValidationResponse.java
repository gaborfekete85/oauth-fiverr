package com.aks.gateway.filter.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenValidationResponse {

    @JsonProperty("active")
    public boolean active;

    public TokenValidationResponse() {
    }

    public TokenValidationResponse(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
