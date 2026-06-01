package com.lunarbase.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private boolean sucesso;
    private String mensagem;
    private T dados;
    private LocalDateTime timestamp;

    public static <T> ApiResponse<T> ok(T dados) {
        return ApiResponse.<T>builder()
                .sucesso(true)
                .dados(dados)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> ok(String mensagem, T dados) {
        return ApiResponse.<T>builder()
                .sucesso(true)
                .mensagem(mensagem)
                .dados(dados)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> erro(String mensagem) {
        return ApiResponse.<T>builder()
                .sucesso(false)
                .mensagem(mensagem)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
