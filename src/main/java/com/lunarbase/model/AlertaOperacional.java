package com.lunarbase.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "alertas_operacionais")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertaOperacional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Título do alerta é obrigatório")
    @Size(max = 150)
    @Column(nullable = false)
    private String titulo;

    @NotBlank(message = "Descrição do alerta é obrigatória")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @NotNull(message = "Severidade é obrigatória")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeveridadeAlerta severidade;

    @NotNull(message = "Tipo do alerta é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAlerta tipoAlerta;

    // Tipos de notificação — VISUAL, SONORO ou ambos
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_notificacao")
    @Builder.Default
    private TipoNotificacao tipoNotificacao = TipoNotificacao.VISUAL;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatusAlerta status = StatusAlerta.ATIVO;

    // ID do recurso que originou o alerta (sensor, reservatório, etc.)
    @Column(name = "recurso_id")
    private Long recursoId;

    // Nome do recurso para facilitar leitura
    @Size(max = 100)
    @Column(name = "recurso_nome")
    private String recursoNome;

    // Valor que disparou o alerta
    @Column(name = "valor_disparador")
    private Double valorDisparador;

    // Valor limite configurado
    @Column(name = "valor_limite")
    private Double valorLimite;

    @Column(name = "setor")
    private String setor;

    // Quem reconheceu/resolveu
    @Column(name = "reconhecido_por")
    private String reconhecidoPor;

    @Column(name = "reconhecido_em")
    private LocalDateTime reconhecidoEm;

    @Column(name = "resolvido_em")
    private LocalDateTime resolvidoEm;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

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
