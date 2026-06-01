package com.lunarbase.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "climatizacao")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Climatizacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome do sistema é obrigatório")
    @Size(max = 100)
    @Column(nullable = false)
    private String nome;

    @NotNull(message = "Tipo do sistema é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoSistema tipoSistema;

    @NotBlank(message = "Localização é obrigatória")
    @Column(nullable = false)
    private String localizacao;

    // VENTILAÇÃO: velocidade em m³/h | ILUMINAÇÃO: intensidade em % | BOMBA: vazão em L/min | VALVULA: abertura em %
    @Min(0) @Max(100)
    @Column(name = "intensidade_percentual")
    @Builder.Default
    private Integer intensidadePercentual = 0;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatusAtuador status = StatusAtuador.DESLIGADO;

    // Temperatura alvo para ventilação (°C)
    @Column(name = "temperatura_alvo")
    private Double temperaturaAlvo;

    // Temperatura atual medida (°C)
    @Column(name = "temperatura_atual")
    private Double temperaturaAtual;

    // Umidade alvo para ventilação (%)
    @Column(name = "umidade_alvo")
    private Double umidadeAlvo;

    // Modo automático — ajusta intensidade baseado nos sensores
    @Column(name = "modo_automatico")
    @Builder.Default
    private Boolean modoAutomatico = false;

    // Consumo elétrico deste sistema (W)
    @Column(name = "consumo_watts")
    private Double consumoWatts;

    @Column(name = "ligado_em")
    private LocalDateTime ligadoEm;

    @Column(name = "desligado_em")
    private LocalDateTime desligadoEm;

    @Column(name = "criado_em", updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @PrePersist
    protected void onCreate() {
        criadoEm = LocalDateTime.now();
        atualizadoEm = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        atualizadoEm = LocalDateTime.now();
    }
}
