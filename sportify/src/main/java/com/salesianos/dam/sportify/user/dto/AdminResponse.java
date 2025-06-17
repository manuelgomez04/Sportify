package com.salesianos.dam.sportify.user.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.salesianos.dam.sportify.user.model.User;

public record AdminResponse(
     UUID id,
        String username,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String token,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String refreshToken,
        String activationToken
) {
  public static AdminResponse of (User user) {
        return new AdminResponse(user.getId(), user.getUsername(), null, null, user.getActivationToken());
    }

    public static AdminResponse of (User user, String token, String refreshToken) {
        return new AdminResponse(user.getId(), user.getUsername(), token, refreshToken, user.getActivationToken());
    }
}
