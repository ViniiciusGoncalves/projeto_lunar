package com.lunarbase.service;

import com.lunarbase.dto.LeituraDTO;
import com.lunarbase.dto.SensorDTO;
import com.lunarbase.exception.RecursoNaoEncontradoException;
import com.lunarbase.model.*;
import com.lunarbase.repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SensorService {

    private final SensorRepository sensorRepository;
    private final AlertaOperacionalService alertaService;

    @Transactional
    public SensorDTO criar(SensorDTO dto) {
        Sensor sensor = toEntity(dto);
        sensor = sensorRepository.save(sensor);
        return toDTO(sensor);
    }

    public List<SensorDTO> listarTodos() {
        return sensorRepository.findAll()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public SensorDTO buscarPorId(Long id) {
        return toDTO(buscarEntidade(id));
    }

    public List<SensorDTO> listarPorTipo(TipoSensor tipo) {
        return sensorRepository.findByTipo(tipo)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<SensorDTO> listarPorStatus(StatusSensor status) {
        return sensorRepository.findByStatus(status)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<SensorDTO> listarPorLocalizacao(String localizacao) {
        return sensorRepository.findByLocalizacaoContainingIgnoreCase(localizacao)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<SensorDTO> listarSensoresEmAlerta() {
        List<Sensor> abaixo = sensorRepository.findSensoresAbaixoLimiteMinimo();
        List<Sensor> acima  = sensorRepository.findSensoresAcimaLimiteMaximo();
        abaixo.addAll(acima);
        return abaixo.stream().distinct().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public SensorDTO atualizar(Long id, SensorDTO dto) {
        Sensor sensor = buscarEntidade(id);
        sensor.setNome(dto.getNome());
        sensor.setTipo(dto.getTipo());
        sensor.setLocalizacao(dto.getLocalizacao());
        sensor.setUnidadeMedida(dto.getUnidadeMedida());
        sensor.setStatus(dto.getStatus() != null ? dto.getStatus() : sensor.getStatus());
        sensor.setLimiteMinimo(dto.getLimiteMinimo());
        sensor.setLimiteMaximo(dto.getLimiteMaximo());
        return toDTO(sensorRepository.save(sensor));
    }

    @Transactional
    public SensorDTO registrarLeitura(Long id, LeituraDTO leitura) {
        Sensor sensor = buscarEntidade(id);
        double valor = leitura.getValorLeitura();
        sensor.setValorLeitura(valor);
        sensor.setUltimaLeitura(
                leitura.getUltimaLeitura() != null ? leitura.getUltimaLeitura() : LocalDateTime.now());

        // Verifica limites e dispara alertas automáticos
        if (sensor.getLimiteMaximo() != null && valor > sensor.getLimiteMaximo()) {
            alertaService.gerarAlertaAutomatico(sensor, valor, sensor.getLimiteMaximo(), true);
        } else if (sensor.getLimiteMinimo() != null && valor < sensor.getLimiteMinimo()) {
            alertaService.gerarAlertaAutomatico(sensor, valor, sensor.getLimiteMinimo(), false);
        }

        return toDTO(sensorRepository.save(sensor));
    }

    @Transactional
    public void deletar(Long id) {
        buscarEntidade(id);
        sensorRepository.deleteById(id);
    }

    private Sensor buscarEntidade(Long id) {
        return sensorRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sensor não encontrado com id: " + id));
    }

    private Sensor toEntity(SensorDTO dto) {
        return Sensor.builder()
                .nome(dto.getNome())
                .tipo(dto.getTipo())
                .localizacao(dto.getLocalizacao())
                .valorLeitura(dto.getValorLeitura())
                .unidadeMedida(dto.getUnidadeMedida())
                .status(dto.getStatus() != null ? dto.getStatus() : StatusSensor.ATIVO)
                .limiteMinimo(dto.getLimiteMinimo())
                .limiteMaximo(dto.getLimiteMaximo())
                .dataInstalacao(dto.getDataInstalacao())
                .ultimaLeitura(dto.getUltimaLeitura())
                .build();
    }

    public SensorDTO toDTO(Sensor s) {
        return SensorDTO.builder()
                .id(s.getId())
                .nome(s.getNome())
                .tipo(s.getTipo())
                .localizacao(s.getLocalizacao())
                .valorLeitura(s.getValorLeitura())
                .unidadeMedida(s.getUnidadeMedida())
                .status(s.getStatus())
                .limiteMinimo(s.getLimiteMinimo())
                .limiteMaximo(s.getLimiteMaximo())
                .dataInstalacao(s.getDataInstalacao())
                .ultimaLeitura(s.getUltimaLeitura())
                .criadoEm(s.getCriadoEm())
                .atualizadoEm(s.getAtualizadoEm())
                .build();
    }
}
