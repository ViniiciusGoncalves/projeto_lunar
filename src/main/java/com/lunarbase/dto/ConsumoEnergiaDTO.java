package com.lunarbase.dto;

import com.lunarbase.model.StatusEnergia;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsumoEnergiaDTO {

    private Long id;

    @NotBlank(message = "Setor é obrigatório")
    private String setor;

    @NotNull @PositiveOrZero
    private Double consumoKwh;

    @PositiveOrZero
    private Double geracaoSolarKwh;

    private Double capacidadeBateriaKwh;

    @Min(0) @Max(100)
    private Double nivelBateriaPercentual;

    private StatusEnergia status;
    private Double limiteConsumoAlerta;
    private Double balancoEnergetico; // calculado
    private LocalDateTime periodoInicio;
    private LocalDateTime periodoFim;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
