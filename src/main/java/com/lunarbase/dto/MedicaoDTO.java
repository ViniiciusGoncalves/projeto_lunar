package com.lunarbase.dto;

import com.lunarbase.model.StatusMedicao;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicaoDTO {

    private Long id;

    @NotNull(message = "O ID do sensor é obrigatório")
    private Long sensorId;

    @NotNull(message = "O valor da medição é obrigatório")
    private Double valor;

    private LocalDateTime data;

    private StatusMedicao status;
}