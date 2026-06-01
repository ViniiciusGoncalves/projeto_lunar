package com.lunarbase.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sensores")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome do sensor é obrigatório")
    @Size(max = 100)
    @Column(nullable = false)
    private String nome;

    @NotNull(message = "Tipo do sensor é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoSensor tipo;

    @NotBlank(message = "Localização é obrigatória")
    @Size(max = 150)
    @Column(nullable = false)
    private String localizacao;

    @Column(name = "valor_leitura")
    private Double valorLeitura;

    @Size(max = 20)
    @Column(name = "unidade_medida")
    private String unidadeMedida;

    @NotNull(message = "Status é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatusSensor status = StatusSensor.ATIVO;

    @Column(name = "limite_minimo")
    private Double limiteMinimo;

    @Column(name = "limite_maximo")
    private Double limiteMaximo;

    @Column(name = "data_instalacao")
    private LocalDateTime dataInstalacao;

    @Column(name = "ultima_leitura")
    private LocalDateTime ultimaLeitura;

    @Column(name = "criado_em", updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @PrePersist
    protected void onCreate() {
        criadoEm = LocalDateTime.now();
        atualizadoEm = LocalDateTime.now();
        if (dataInstalacao == null) dataInstalacao = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        atualizadoEm = LocalDateTime.now();
    }
}
