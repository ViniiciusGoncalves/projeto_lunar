package com.lunarbase.dto;

import com.lunarbase.model.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertaOperacionalDTO {

    private Long id;

    @NotBlank(message = "Título é obrigatório")
    private String titulo;

    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;

    @NotNull(message = "Severidade é obrigatória")
    private SeveridadeAlerta severidade;

    @NotNull(message = "Tipo do alerta é obrigatório")
    private TipoAlerta tipoAlerta;

    private TipoNotificacao tipoNotificacao;
    private StatusAlerta status;
    private Long recursoId;
    private String recursoNome;
    private Double valorDisparador;
    private Double valorLimite;
    private String setor;
    private String reconhecidoPor;
    private LocalDateTime reconhecidoEm;
    private LocalDateTime resolvidoEm;
    private String observacoes;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
