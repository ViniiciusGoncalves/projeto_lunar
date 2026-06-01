package com.lunarbase.service;

import com.lunarbase.dto.ConsumoEnergiaDTO;
import com.lunarbase.exception.RecursoNaoEncontradoException;
import com.lunarbase.model.*;
import com.lunarbase.repository.ConsumoEnergiaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsumoEnergiaService {

    private final ConsumoEnergiaRepository consumoRepository;

    @Transactional
    public ConsumoEnergiaDTO criar(ConsumoEnergiaDTO dto) {
        ConsumoEnergia c = toEntity(dto);
        return toDTO(consumoRepository.save(c));
    }

    public List<ConsumoEnergiaDTO> listarTodos() {
        return consumoRepository.findAll()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ConsumoEnergiaDTO buscarPorId(Long id) {
        return toDTO(buscarEntidade(id));
    }

    public List<ConsumoEnergiaDTO> listarPorStatus(StatusEnergia status) {
        return consumoRepository.findByStatus(status)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<ConsumoEnergiaDTO> listarSetoresAcimaLimite() {
        return consumoRepository.findSetoresAcimaLimite()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public ConsumoEnergiaDTO atualizar(Long id, ConsumoEnergiaDTO dto) {
        ConsumoEnergia c = buscarEntidade(id);
        c.setSetor(dto.getSetor());
        c.setConsumoKwh(dto.getConsumoKwh());
        c.setGeracaoSolarKwh(dto.getGeracaoSolarKwh() != null ? dto.getGeracaoSolarKwh() : c.getGeracaoSolarKwh());
        c.setCapacidadeBateriaKwh(dto.getCapacidadeBateriaKwh());
        c.setNivelBateriaPercentual(dto.getNivelBateriaPercentual());
        c.setLimiteConsumoAlerta(dto.getLimiteConsumoAlerta());
        c.setPeriodoFim(dto.getPeriodoFim());

        // Calcula status automaticamente
        c.setStatus(calcularStatus(c));

        return toDTO(consumoRepository.save(c));
    }

    @Transactional
    public void deletar(Long id) {
        buscarEntidade(id);
        consumoRepository.deleteById(id);
    }

    public Double getConsumoTotal() {
        Double total = consumoRepository.sumConsumoTotal();
        return total != null ? total : 0.0;
    }

    public Double getGeracaoTotal() {
        Double total = consumoRepository.sumGeracaoTotal();
        return total != null ? total : 0.0;
    }

    private StatusEnergia calcularStatus(ConsumoEnergia c) {
        if (c.getNivelBateriaPercentual() != null && c.getNivelBateriaPercentual() < 5)
            return StatusEnergia.BATERIA_CRITICA;
        if (c.getNivelBateriaPercentual() != null && c.getNivelBateriaPercentual() < 15)
            return StatusEnergia.BATERIA_BAIXA;
        if (c.getLimiteConsumoAlerta() != null && c.getConsumoKwh() > c.getLimiteConsumoAlerta() * 1.3)
            return StatusEnergia.CONSUMO_CRITICO;
        if (c.getLimiteConsumoAlerta() != null && c.getConsumoKwh() > c.getLimiteConsumoAlerta())
            return StatusEnergia.CONSUMO_ALTO;
        return StatusEnergia.NORMAL;
    }

    private ConsumoEnergia buscarEntidade(Long id) {
        return consumoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Registro de consumo não encontrado com id: " + id));
    }

    private ConsumoEnergia toEntity(ConsumoEnergiaDTO dto) {
        return ConsumoEnergia.builder()
                .setor(dto.getSetor())
                .consumoKwh(dto.getConsumoKwh())
                .geracaoSolarKwh(dto.getGeracaoSolarKwh() != null ? dto.getGeracaoSolarKwh() : 0.0)
                .capacidadeBateriaKwh(dto.getCapacidadeBateriaKwh())
                .nivelBateriaPercentual(dto.getNivelBateriaPercentual())
                .status(dto.getStatus() != null ? dto.getStatus() : StatusEnergia.NORMAL)
                .limiteConsumoAlerta(dto.getLimiteConsumoAlerta())
                .periodoInicio(dto.getPeriodoInicio())
                .periodoFim(dto.getPeriodoFim())
                .build();
    }

    public ConsumoEnergiaDTO toDTO(ConsumoEnergia c) {
        return ConsumoEnergiaDTO.builder()
                .id(c.getId())
                .setor(c.getSetor())
                .consumoKwh(c.getConsumoKwh())
                .geracaoSolarKwh(c.getGeracaoSolarKwh())
                .capacidadeBateriaKwh(c.getCapacidadeBateriaKwh())
                .nivelBateriaPercentual(c.getNivelBateriaPercentual())
                .status(c.getStatus())
                .limiteConsumoAlerta(c.getLimiteConsumoAlerta())
                .balancoEnergetico(c.getBalancoEnergetico())
                .periodoInicio(c.getPeriodoInicio())
                .periodoFim(c.getPeriodoFim())
                .criadoEm(c.getCriadoEm())
                .atualizadoEm(c.getAtualizadoEm())
                .build();
    }

    public long contarBateriaCritica() {
        return consumoRepository.findSetoresBateriaCritica().size();
    }
}
