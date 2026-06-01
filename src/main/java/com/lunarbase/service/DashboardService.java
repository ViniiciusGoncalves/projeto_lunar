package com.lunarbase.service;

import com.lunarbase.dto.DashboardDTO;
import com.lunarbase.model.*;
import com.lunarbase.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final SensorRepository sensorRepository;
    private final ReservatorioRepository reservatorioRepository;
    private final ConsumoEnergiaRepository consumoRepository;
    private final ClimatizacaoRepository climatizacaoRepository;
    private final AlertaOperacionalRepository alertaRepository;

    public DashboardDTO gerarDashboard() {
        // Sensores
        long totalSensores = sensorRepository.count();
        long sensoresAtivos = sensorRepository.findByStatus(StatusSensor.ATIVO).size();
        long sensoresFalha  = sensorRepository.findByStatus(StatusSensor.FALHA).size();
        long sensoresAlerta = sensorRepository.findSensoresAbaixoLimiteMinimo().size()
                            + sensorRepository.findSensoresAcimaLimiteMaximo().size();

        // Reservatórios
        long totalReservatorios = reservatorioRepository.count();
        long reservatoriosNivelBaixo = reservatorioRepository.findReservatoriosNivelBaixo().size();
        Double totalAgua = reservatorioRepository.sumNivelAtualByTipoLiquido(TipoLiquido.AGUA_POTAVEL);
        double pctMedioAgua = reservatorioRepository.findAll().stream()
                .mapToDouble(r -> r.getNivelPercentual() != null ? r.getNivelPercentual() : 0)
                .average().orElse(0.0);

        // Energia
        Double consumoTotal  = consumoRepository.sumConsumoTotal();
        Double geracaoTotal  = consumoRepository.sumGeracaoTotal();
        double consumo = consumoTotal  != null ? consumoTotal  : 0.0;
        double geracao = geracaoTotal  != null ? geracaoTotal  : 0.0;
        long setoresBateriaCritica = consumoRepository.findSetoresBateriaCritica().size();

        // Climatização
        long sistemasLigados = climatizacaoRepository.findByStatus(StatusAtuador.LIGADO).size();
        long sistemasEmFalha = climatizacaoRepository.findByStatus(StatusAtuador.FALHA).size();
        Double consumoClim = climatizacaoRepository.sumConsumoAtivo();

        // Alertas
        long alertasAtivos    = alertaRepository.countByStatus(StatusAlerta.ATIVO);
        long alertasCriticos  = alertaRepository.countBySeveridadeAndStatus(SeveridadeAlerta.CRITICO,    StatusAlerta.ATIVO);
        long alertasEmergencia= alertaRepository.countBySeveridadeAndStatus(SeveridadeAlerta.EMERGENCIA, StatusAlerta.ATIVO);

        return DashboardDTO.builder()
                .totalSensores(totalSensores)
                .sensoresAtivos(sensoresAtivos)
                .sensoresFalha(sensoresFalha)
                .sensoresAlerta(sensoresAlerta)
                .totalReservatorios(totalReservatorios)
                .reservatoriosNivelBaixo(reservatoriosNivelBaixo)
                .totalAguaLitros(totalAgua != null ? totalAgua : 0.0)
                .percentualMedioAgua(pctMedioAgua)
                .consumoTotalKwh(consumo)
                .geracaoTotalKwh(geracao)
                .balancoEnergeticoKwh(geracao - consumo)
                .setoresBateriaCritica(setoresBateriaCritica)
                .sistemasLigados(sistemasLigados)
                .sistemasEmFalha(sistemasEmFalha)
                .consumoClimatizacaoWatts(consumoClim != null ? consumoClim : 0.0)
                .alertasAtivos(alertasAtivos)
                .alertasCriticos(alertasCriticos)
                .alertasEmergencia(alertasEmergencia)
                .geradoEm(LocalDateTime.now())
                .build();
    }
}
