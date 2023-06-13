package com.example.demo.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AllowedUri {

    LOGIN("/auth/login"),
    REGISTER("/auth/register"),
    SWAGGER("/swagger-ui/index.html");

    private final String uri;
}
