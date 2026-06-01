package com.lunarbase.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardDTO {

    // Sensores
    private long totalSensores;
    private long sensoresAtivos;
    private long sensoresFalha;
    private long sensoresAlerta;

    // Reservatórios
    private long totalReservatorios;
    private long reservatoriosNivelBaixo;
    private Double totalAguaLitros;
    private Double percentualMedioAgua;

    // Energia
    private Double consumoTotalKwh;
    private Double geracaoTotalKwh;
    private Double balancoEnergeticoKwh;
    private long setoresBateriaCritica;

    // Climatização
    private long sistemasLigados;
    private long sistemasEmFalha;
    private Double consumoClimatizacaoWatts;

    // Alertas
    private long alertasAtivos;
    private long alertasCriticos;
    private long alertasEmergencia;

    private LocalDateTime geradoEm;
}
