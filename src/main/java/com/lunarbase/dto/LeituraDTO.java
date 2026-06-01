package com.lunarbase.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeituraDTO {

    @NotNull(message = "Valor de leitura é obrigatório")
    private Double valorLeitura;

    private LocalDateTime ultimaLeitura;
}
