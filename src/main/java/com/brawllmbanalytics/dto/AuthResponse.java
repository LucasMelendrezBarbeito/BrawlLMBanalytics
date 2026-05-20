package com.brawllmbanalytics.dto;

public record AuthResponse(
        String token,
        Integer userId,
        String username) {
}
