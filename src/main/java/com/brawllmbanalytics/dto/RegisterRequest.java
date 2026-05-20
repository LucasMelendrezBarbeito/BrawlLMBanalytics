package com.brawllmbanalytics.dto;


public record RegisterRequest(
    String username,
    String email,
    String password
) {
}