package com.lunarbase.config;

import com.lunarbase.model.*;
import com.lunarbase.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    @Bean
    CommandLineRunner initData(
            SensorRepository sensorRepo,
            ReservatorioRepository reservatorioRepo,
            ConsumoEnergiaRepository consumoRepo,
            ClimatizacaoRepository climatizacaoRepo,
            AlertaOperacionalRepository alertaRepo) {

        return args -> {
            if (sensorRepo.count() > 0) {
                log.info("🌕 Banco de dados já populado — pulando inicialização.");
                return;
            }

            log.info("🚀 Inicializando dados da Base Lunar...");

            // ─── SENSORES ────────────────────────────────────────────────
            sensorRepo.save(Sensor.builder()
                    .nome("Termômetro Módulo Habitacional A")
                    .tipo(TipoSensor.TEMPERATURA)
                    .localizacao("Módulo Habitacional A")
                    .valorLeitura(22.5).unidadeMedida("°C")
                    .limiteMinimo(15.0).limiteMaximo(35.0)
                    .status(StatusSensor.ATIVO).build());

            sensorRepo.save(Sensor.builder()
                    .nome("Termômetro Câmara de Airlock")
                    .tipo(TipoSensor.TEMPERATURA)
                    .localizacao("Câmara de Airlock")
                    .valorLeitura(-10.0).unidadeMedida("°C")
                    .limiteMinimo(-50.0).limiteMaximo(40.0)
                    .status(StatusSensor.ATIVO).build());

            sensorRepo.save(Sensor.builder()
                    .nome("Sensor de Umidade Central")
                    .tipo(TipoSensor.UMIDADE)
                    .localizacao("Módulo Central")
                    .valorLeitura(45.0).unidadeMedida("%")
                    .limiteMinimo(30.0).limiteMaximo(70.0)
                    .status(StatusSensor.ATIVO).build());

            sensorRepo.save(Sensor.builder()
                    .nome("Fotossensor Estufa")
                    .tipo(TipoSensor.LUMINOSIDADE)
                    .localizacao("Módulo Estufa")
                    .valorLeitura(2500.0).unidadeMedida("lux")
                    .limiteMinimo(1000.0).limiteMaximo(10000.0)
                    .status(StatusSensor.ATIVO).build());

            sensorRepo.save(Sensor.builder()
                    .nome("Sensor Nível Reservatório Principal")
                    .tipo(TipoSensor.NIVEL_AGUA)
                    .localizacao("Reservatório Principal")
                    .valorLeitura(78.0).unidadeMedida("%")
                    .limiteMinimo(20.0).limiteMaximo(98.0)
                    .status(StatusSensor.ATIVO).build());

            sensorRepo.save(Sensor.builder()
                    .nome("Barômetro Módulo Central")
                    .tipo(TipoSensor.PRESSAO_ATMOSFERICA)
                    .localizacao("Módulo Central")
                    .valorLeitura(101.3).unidadeMedida("kPa")
                    .limiteMinimo(95.0).limiteMaximo(110.0)
                    .status(StatusSensor.ATIVO).build());

            sensorRepo.save(Sensor.builder()
                    .nome("Sensor CO₂ Laboratório")
                    .tipo(TipoSensor.QUALIDADE_AR)
                    .localizacao("Módulo Laboratório")
                    .valorLeitura(420.0).unidadeMedida("ppm")
                    .limiteMinimo(0.0).limiteMaximo(1000.0)
                    .status(StatusSensor.ATIVO).build());

            sensorRepo.save(Sensor.builder()
                    .nome("Radiômetro Superfície")
                    .tipo(TipoSensor.RADIACAO_SOLAR)
                    .localizacao("Topo — Superfície Lunar")
                    .valorLeitura(1361.0).unidadeMedida("W/m²")
                    .limiteMinimo(0.0).limiteMaximo(1500.0)
                    .status(StatusSensor.ATIVO).build());

            sensorRepo.save(Sensor.builder()
                    .nome("Monitor de Consumo — Setor A")
                    .tipo(TipoSensor.CONSUMO_ENERGIA)
                    .localizacao("Setor A")
                    .valorLeitura(18.5).unidadeMedida("kWh")
                    .limiteMinimo(0.0).limiteMaximo(50.0)
                    .status(StatusSensor.ATIVO).build());

            sensorRepo.save(Sensor.builder()
                    .nome("Sensor Bateria — Banco 1")
                    .tipo(TipoSensor.NIVEL_BATERIA)
                    .localizacao("Sala de Energia")
                    .valorLeitura(82.0).unidadeMedida("%")
                    .limiteMinimo(15.0).limiteMaximo(100.0)
                    .status(StatusSensor.ATIVO).build());

            // ─── RESERVATÓRIOS ───────────────────────────────────────────
            reservatorioRepo.save(Reservatorio.builder()
                    .nome("Reservatório de Água Potável Principal")
                    .capacidadeMaxima(50000.0).nivelAtual(38500.0)
                    .localizacao("Módulo de Suporte de Vida")
                    .tipoLiquido(TipoLiquido.AGUA_POTAVEL)
                    .status(StatusReservatorio.NORMAL)
                    .alertaNivelMinimo(20.0).alertaNivelMaximo(95.0)
                    .ultimaRecarga(LocalDateTime.now().minusDays(3)).build());

            reservatorioRepo.save(Reservatorio.builder()
                    .nome("Reservatório de Água Destilada — Laboratório")
                    .capacidadeMaxima(5000.0).nivelAtual(4200.0)
                    .localizacao("Módulo Laboratório")
                    .tipoLiquido(TipoLiquido.AGUA_DESTILADA)
                    .status(StatusReservatorio.NORMAL)
                    .alertaNivelMinimo(15.0).alertaNivelMaximo(98.0).build());

            reservatorioRepo.save(Reservatorio.builder()
                    .nome("Tanque de Combustível — Módulo Propulsão")
                    .capacidadeMaxima(10000.0).nivelAtual(1500.0)
                    .localizacao("Módulo de Propulsão")
                    .tipoLiquido(TipoLiquido.COMBUSTIVEL)
                    .status(StatusReservatorio.NIVEL_BAIXO)
                    .alertaNivelMinimo(20.0).alertaNivelMaximo(95.0).build());

            reservatorioRepo.save(Reservatorio.builder()
                    .nome("Reservatório Água Residual")
                    .capacidadeMaxima(20000.0).nivelAtual(8000.0)
                    .localizacao("Módulo de Tratamento")
                    .tipoLiquido(TipoLiquido.AGUA_RESIDUAL)
                    .status(StatusReservatorio.NORMAL).build());

            // ─── CONSUMO DE ENERGIA ──────────────────────────────────────
            consumoRepo.save(ConsumoEnergia.builder()
                    .setor("Módulo Habitacional A")
                    .consumoKwh(12.4).geracaoSolarKwh(8.0)
                    .capacidadeBateriaKwh(100.0).nivelBateriaPercentual(82.0)
                    .status(StatusEnergia.NORMAL).limiteConsumoAlerta(20.0)
                    .periodoInicio(LocalDateTime.now().minusHours(1)).build());

            consumoRepo.save(ConsumoEnergia.builder()
                    .setor("Módulo Laboratório")
                    .consumoKwh(31.2).geracaoSolarKwh(15.0)
                    .capacidadeBateriaKwh(100.0).nivelBateriaPercentual(55.0)
                    .status(StatusEnergia.CONSUMO_ALTO).limiteConsumoAlerta(25.0)
                    .periodoInicio(LocalDateTime.now().minusHours(2)).build());

            consumoRepo.save(ConsumoEnergia.builder()
                    .setor("Módulo de Suporte de Vida")
                    .consumoKwh(18.7).geracaoSolarKwh(20.0)
                    .capacidadeBateriaKwh(150.0).nivelBateriaPercentual(91.0)
                    .status(StatusEnergia.NORMAL).limiteConsumoAlerta(30.0)
                    .periodoInicio(LocalDateTime.now().minusHours(1)).build());

            consumoRepo.save(ConsumoEnergia.builder()
                    .setor("Sala de Controle")
                    .consumoKwh(9.1).geracaoSolarKwh(5.0)
                    .capacidadeBateriaKwh(80.0).nivelBateriaPercentual(12.0)
                    .status(StatusEnergia.BATERIA_BAIXA).limiteConsumoAlerta(15.0)
                    .periodoInicio(LocalDateTime.now().minusHours(1)).build());

            // ─── CLIMATIZAÇÃO ────────────────────────────────────────────
            climatizacaoRepo.save(Climatizacao.builder()
                    .nome("Sistema de Ventilação Central")
                    .tipoSistema(TipoSistema.VENTILACAO)
                    .localizacao("Módulo Central")
                    .intensidadePercentual(65).status(StatusAtuador.LIGADO)
                    .temperaturaAlvo(22.0).temperaturaAtual(23.1)
                    .umidadeAlvo(45.0).modoAutomatico(true)
                    .consumoWatts(450.0).ligadoEm(LocalDateTime.now().minusHours(8)).build());

            climatizacaoRepo.save(Climatizacao.builder()
                    .nome("Iluminação LED — Habitacional A")
                    .tipoSistema(TipoSistema.ILUMINACAO)
                    .localizacao("Módulo Habitacional A")
                    .intensidadePercentual(80).status(StatusAtuador.LIGADO)
                    .modoAutomatico(false).consumoWatts(120.0)
                    .ligadoEm(LocalDateTime.now().minusHours(5)).build());

            climatizacaoRepo.save(Climatizacao.builder()
                    .nome("Bomba de Água — Distribuição Principal")
                    .tipoSistema(TipoSistema.BOMBA_AGUA)
                    .localizacao("Módulo de Suporte de Vida")
                    .intensidadePercentual(50).status(StatusAtuador.LIGADO)
                    .modoAutomatico(true).consumoWatts(380.0)
                    .ligadoEm(LocalDateTime.now().minusHours(2)).build());

            climatizacaoRepo.save(Climatizacao.builder()
                    .nome("Válvula de Água — Estufa")
                    .tipoSistema(TipoSistema.VALVULA_AGUA)
                    .localizacao("Módulo Estufa")
                    .intensidadePercentual(0).status(StatusAtuador.DESLIGADO)
                    .modoAutomatico(false).consumoWatts(15.0).build());

            climatizacaoRepo.save(Climatizacao.builder()
                    .nome("Exaustor — Laboratório")
                    .tipoSistema(TipoSistema.EXAUSTOR)
                    .localizacao("Módulo Laboratório")
                    .intensidadePercentual(100).status(StatusAtuador.LIGADO)
                    .modoAutomatico(false).consumoWatts(200.0)
                    .ligadoEm(LocalDateTime.now().minusHours(1)).build());

            climatizacaoRepo.save(Climatizacao.builder()
                    .nome("Painel Solar — Array Norte")
                    .tipoSistema(TipoSistema.PAINEL_SOLAR)
                    .localizacao("Topo Norte")
                    .intensidadePercentual(90).status(StatusAtuador.LIGADO)
                    .modoAutomatico(true).consumoWatts(0.0)
                    .ligadoEm(LocalDateTime.now().minusHours(12)).build());

            // ─── ALERTAS ─────────────────────────────────────────────────
            alertaRepo.save(AlertaOperacional.builder()
                    .titulo("Nível Crítico — Tanque de Combustível")
                    .descricao("O tanque de combustível do Módulo de Propulsão atingiu 15% da capacidade total. Reabastecimento urgente necessário.")
                    .severidade(SeveridadeAlerta.CRITICO)
                    .tipoAlerta(TipoAlerta.NIVEL_AGUA_CRITICO)
                    .tipoNotificacao(TipoNotificacao.VISUAL_E_SONORO)
                    .status(StatusAlerta.ATIVO)
                    .recursoNome("Tanque de Combustível — Módulo Propulsão")
                    .valorDisparador(15.0).valorLimite(20.0)
                    .setor("Módulo de Propulsão").build());

            alertaRepo.save(AlertaOperacional.builder()
                    .titulo("Bateria Baixa — Sala de Controle")
                    .descricao("Nível de bateria da Sala de Controle em 12%. Risco de falha de sistemas críticos.")
                    .severidade(SeveridadeAlerta.AVISO)
                    .tipoAlerta(TipoAlerta.BATERIA_BAIXA)
                    .tipoNotificacao(TipoNotificacao.VISUAL)
                    .status(StatusAlerta.ATIVO)
                    .recursoNome("Sala de Controle")
                    .valorDisparador(12.0).valorLimite(15.0)
                    .setor("Sala de Controle").build());

            alertaRepo.save(AlertaOperacional.builder()
                    .titulo("Consumo Elevado — Módulo Laboratório")
                    .descricao("Consumo do Módulo Laboratório (31.2 kWh) excede o limite configurado de 25 kWh.")
                    .severidade(SeveridadeAlerta.AVISO)
                    .tipoAlerta(TipoAlerta.CONSUMO_ENERGIA_ALTO)
                    .tipoNotificacao(TipoNotificacao.VISUAL)
                    .status(StatusAlerta.RECONHECIDO)
                    .reconhecidoPor("Operador Delta").reconhecidoEm(LocalDateTime.now().minusMinutes(30))
                    .recursoNome("Módulo Laboratório")
                    .valorDisparador(31.2).valorLimite(25.0)
                    .setor("Módulo Laboratório").build());

            log.info("✅ Base Lunar inicializada: {} sensores | {} reservatórios | {} registros de energia | {} sistemas | {} alertas",
                    sensorRepo.count(), reservatorioRepo.count(), consumoRepo.count(),
                    climatizacaoRepo.count(), alertaRepo.count());
        };
    }
}
