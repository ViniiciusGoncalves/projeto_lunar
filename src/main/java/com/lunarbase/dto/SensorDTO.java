package com.lunarbase.dto;

import com.lunarbase.model.StatusSensor;
import com.lunarbase.model.TipoSensor;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SensorDTO {

    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotNull(message = "Tipo é obrigatório")
    private TipoSensor tipo;

    @NotBlank(message = "Localização é obrigatória")
    private String localizacao;

    private Double valorLeitura;
    private String unidadeMedida;
    private StatusSensor status;
    private Double limiteMinimo;
    private Double limiteMaximo;
    private LocalDateTime dataInstalacao;
    private LocalDateTime ultimaLeitura;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
