package com.brawllmbanalytics.dto;


public record LoginRequest(
    String username,
    String password
) {
}