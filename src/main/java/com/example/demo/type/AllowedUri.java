package com.example.demo.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AllowedUri {

    LOGIN("/auth/login"),
    REGISTER("/auth/register"),
    SWAGGER_UI("/swagger-ui/**"),
    API_DOCS("/v3/api-docs/**");

    private final String uri;
}
