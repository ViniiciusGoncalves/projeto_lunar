package com.lunarbase.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservatorios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservatorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome do reservatório é obrigatório")
    @Size(max = 100)
    @Column(nullable = false)
    private String nome;

    @NotNull(message = "Capacidade máxima é obrigatória")
    @Positive(message = "Capacidade deve ser positiva")
    @Column(name = "capacidade_maxima", nullable = false)
    private Double capacidadeMaxima; // em litros

    @Min(0)
    @Column(name = "nivel_atual")
    @Builder.Default
    private Double nivelAtual = 0.0; // em litros

    @NotBlank(message = "Localização é obrigatória")
    @Column(nullable = false)
    private String localizacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private TipoLiquido tipoLiquido = TipoLiquido.AGUA_POTAVEL;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatusReservatorio status = StatusReservatorio.NORMAL;

    // Percentual mínimo antes de disparar alerta (ex: 20.0 = 20%)
    @Column(name = "alerta_nivel_minimo")
    @Builder.Default
    private Double alertaNivelMinimo = 20.0;

    // Percentual máximo antes de disparar alerta de overflow (ex: 95.0 = 95%)
    @Column(name = "alerta_nivel_maximo")
    @Builder.Default
    private Double alertaNivelMaximo = 95.0;

    @Column(name = "ultima_recarga")
    private LocalDateTime ultimaRecarga;

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

    // Calcula percentual de nível
    @Transient
    public Double getNivelPercentual() {
        if (capacidadeMaxima == null || capacidadeMaxima == 0) return 0.0;
        return (nivelAtual / capacidadeMaxima) * 100.0;
    }
}
