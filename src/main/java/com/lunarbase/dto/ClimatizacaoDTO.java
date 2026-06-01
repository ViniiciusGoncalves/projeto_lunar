package com.lunarbase.dto;

import com.lunarbase.model.StatusAtuador;
import com.lunarbase.model.TipoSistema;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClimatizacaoDTO {

    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotNull(message = "Tipo do sistema é obrigatório")
    private TipoSistema tipoSistema;

    @NotBlank(message = "Localização é obrigatória")
    private String localizacao;

    @Min(0) @Max(100)
    private Integer intensidadePercentual;

    private StatusAtuador status;
    private Double temperaturaAlvo;
    private Double temperaturaAtual;
    private Double umidadeAlvo;
    private Boolean modoAutomatico;
    private Double consumoWatts;
    private LocalDateTime ligadoEm;
    private LocalDateTime desligadoEm;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
