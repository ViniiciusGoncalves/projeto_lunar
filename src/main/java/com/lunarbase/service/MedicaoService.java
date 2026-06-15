package com.lunarbase.service;

import com.lunarbase.dto.MedicaoDTO;
import com.lunarbase.exception.RecursoNaoEncontradoException;
import com.lunarbase.model.Medicao;
import com.lunarbase.model.Sensor;
import com.lunarbase.model.StatusMedicao;
import com.lunarbase.repository.MedicaoRepository;
import com.lunarbase.repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicaoService {

    private final MedicaoRepository medicaoRepository;
    private final SensorRepository sensorRepository;

    @Transactional
    public MedicaoDTO registrar(MedicaoDTO dto) {
        Sensor sensor = sensorRepository.findById(dto.getSensorId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sensor não encontrado com id: " + dto.getSensorId()));

        StatusMedicao statusCalculado = calcularStatus(sensor, dto.getValor());

        Medicao medicao = Medicao.builder()
                .sensor(sensor)
                .valor(dto.getValor())
                .data(dto.getData() != null ? dto.getData() : LocalDateTime.now())
                .status(statusCalculado)
                .build();

        sensor.setValorLeitura(dto.getValor());
        sensor.setUltimaLeitura(medicao.getData());
        sensorRepository.save(sensor);

        medicao = medicaoRepository.save(medicao);
        return toDTO(medicao);
    }

    public List<MedicaoDTO> listarTodas() {
        return medicaoRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public MedicaoDTO buscarPorId(Long id) {
        Medicao medicao = medicaoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Medição não encontrada com id: " + id));
        return toDTO(medicao);
    }

    public List<MedicaoDTO> listarPorSensor(Long sensorId) {
        if (!sensorRepository.existsById(sensorId)) {
            throw new RecursoNaoEncontradoException("Sensor não encontrado com id: " + sensorId);
        }
        return medicaoRepository.findBySensorId(sensorId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    // --- REGRA DE NEGÓCIO: INTELIGÊNCIA DE STATUS ---
    private StatusMedicao calcularStatus(Sensor sensor, Double valor) {
        if (sensor.getLimiteMinimo() == null || sensor.getLimiteMaximo() == null) {
            return StatusMedicao.NORMAL;
        }

        double min = sensor.getLimiteMinimo();
        double max = sensor.getLimiteMaximo();

        // Define que "próximo do limite" é estar a 10% da faixa total das extremidades
        double margem = (max - min) * 0.10;

        if (valor < min || valor > max) {
            return StatusMedicao.CRITICO; // Fora do limite
        } else if (valor <= (min + margem) || valor >= (max - margem)) {
            return StatusMedicao.ALERTA;  // Próximo do limite
        } else {
            return StatusMedicao.NORMAL;  // Dentro da faixa segura
        }
    }

    private MedicaoDTO toDTO(Medicao m) {
        return MedicaoDTO.builder()
                .id(m.getId())
                .sensorId(m.getSensor().getId())
                .valor(m.getValor())
                .data(m.getData())
                .status(m.getStatus())
                .build();
    }
}