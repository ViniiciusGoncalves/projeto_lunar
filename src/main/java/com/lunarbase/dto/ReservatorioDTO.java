package com.lunarbase.dto;

import com.lunarbase.model.StatusReservatorio;
import com.lunarbase.model.TipoLiquido;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservatorioDTO {

    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotNull(message = "Capacidade máxima é obrigatória")
    @Positive
    private Double capacidadeMaxima;

    @PositiveOrZero
    private Double nivelAtual;

    private Double nivelPercentual; // calculado

    @NotBlank(message = "Localização é obrigatória")
    private String localizacao;

    private TipoLiquido tipoLiquido;
    private StatusReservatorio status;
    private Double alertaNivelMinimo;
    private Double alertaNivelMaximo;
    private LocalDateTime ultimaRecarga;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
