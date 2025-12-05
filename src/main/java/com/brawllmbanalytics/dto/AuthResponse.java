package com.brawllmbanalytics.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private Integer userId;
    private String username;
}
