package com.example.backend.Dto.Securite;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginRequest(@Schema(example = "jean@mail.com") String email, @Schema(example = "chaton") String password) {
}
