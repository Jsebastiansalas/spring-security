package com.demodd.infrastructure.iam.user.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginWebRequest(
        @NotBlank(message = "El nombre de usuario es obligatorio")
        String username,

        @NotBlank(message = "La contrasena es obligatoria")
        String password
) {}
