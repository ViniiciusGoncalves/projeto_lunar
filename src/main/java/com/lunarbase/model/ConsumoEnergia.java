package com.lunarbase.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "consumo_energia")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsumoEnergia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Setor é obrigatório")
    @Size(max = 100)
    @Column(nullable = false)
    private String setor;

    @NotNull(message = "Consumo é obrigatório")
    @PositiveOrZero(message = "Consumo deve ser positivo ou zero")
    @Column(name = "consumo_kwh", nullable = false)
    private Double consumoKwh;

    @PositiveOrZero
    @Column(name = "geracao_solar_kwh")
    @Builder.Default
    private Double geracaoSolarKwh = 0.0;

    @PositiveOrZero
    @Column(name = "capacidade_bateria_kwh")
    private Double capacidadeBateriaKwh;

    @Min(0) @Max(100)
    @Column(name = "nivel_bateria_percentual")
    private Double nivelBateriaPercentual;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatusEnergia status = StatusEnergia.NORMAL;

    // Limite de consumo configurável para alerta (kWh)
    @Column(name = "limite_consumo_alerta")
    private Double limiteConsumoAlerta;

    @Column(name = "periodo_inicio")
    private LocalDateTime periodoInicio;

    @Column(name = "periodo_fim")
    private LocalDateTime periodoFim;

    @Column(name = "criado_em", updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @PrePersist
    protected void onCreate() {
        criadoEm = LocalDateTime.now();
        atualizadoEm = LocalDateTime.now();
        if (periodoInicio == null) periodoInicio = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        atualizadoEm = LocalDateTime.now();
    }

    @Transient
    public Double getBalancoEnergetico() {
        return geracaoSolarKwh - consumoKwh;
    }
}
