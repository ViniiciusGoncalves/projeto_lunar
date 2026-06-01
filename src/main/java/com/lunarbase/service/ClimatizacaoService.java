package com.lunarbase.service;

import com.lunarbase.dto.ClimatizacaoDTO;
import com.lunarbase.exception.RecursoNaoEncontradoException;
import com.lunarbase.model.*;
import com.lunarbase.repository.ClimatizacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClimatizacaoService {

    private final ClimatizacaoRepository climatizacaoRepository;

    @Transactional
    public ClimatizacaoDTO criar(ClimatizacaoDTO dto) {
        Climatizacao c = toEntity(dto);
        return toDTO(climatizacaoRepository.save(c));
    }

    public List<ClimatizacaoDTO> listarTodos() {
        return climatizacaoRepository.findAll()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ClimatizacaoDTO buscarPorId(Long id) {
        return toDTO(buscarEntidade(id));
    }

    public List<ClimatizacaoDTO> listarPorTipo(TipoSistema tipo) {
        return climatizacaoRepository.findByTipoSistema(tipo)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<ClimatizacaoDTO> listarPorStatus(StatusAtuador status) {
        return climatizacaoRepository.findByStatus(status)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public ClimatizacaoDTO atualizar(Long id, ClimatizacaoDTO dto) {
        Climatizacao c = buscarEntidade(id);
        c.setNome(dto.getNome());
        c.setTipoSistema(dto.getTipoSistema());
        c.setLocalizacao(dto.getLocalizacao());
        c.setTemperaturaAlvo(dto.getTemperaturaAlvo());
        c.setTemperaturaAtual(dto.getTemperaturaAtual());
        c.setUmidadeAlvo(dto.getUmidadeAlvo());
        c.setConsumoWatts(dto.getConsumoWatts());
        if (dto.getIntensidadePercentual() != null) c.setIntensidadePercentual(dto.getIntensidadePercentual());
        if (dto.getModoAutomatico() != null) c.setModoAutomatico(dto.getModoAutomatico());
        if (dto.getStatus() != null) c.setStatus(dto.getStatus());
        return toDTO(climatizacaoRepository.save(c));
    }

    @Transactional
    public ClimatizacaoDTO acionar(Long id, boolean ligar, Integer intensidade) {
        Climatizacao c = buscarEntidade(id);
        if (ligar) {
            c.setStatus(StatusAtuador.LIGADO);
            c.setLigadoEm(LocalDateTime.now());
            c.setDesligadoEm(null);
            if (intensidade != null) c.setIntensidadePercentual(intensidade);
        } else {
            c.setStatus(StatusAtuador.DESLIGADO);
            c.setDesligadoEm(LocalDateTime.now());
            c.setIntensidadePercentual(0);
        }
        return toDTO(climatizacaoRepository.save(c));
    }

    @Transactional
    public ClimatizacaoDTO ajustarIntensidade(Long id, Integer intensidade) {
        if (intensidade < 0 || intensidade > 100)
            throw new IllegalArgumentException("Intensidade deve ser entre 0 e 100");
        Climatizacao c = buscarEntidade(id);
        c.setIntensidadePercentual(intensidade);
        if (intensidade == 0) {
            c.setStatus(StatusAtuador.DESLIGADO);
            c.setDesligadoEm(LocalDateTime.now());
        } else if (c.getStatus() == StatusAtuador.DESLIGADO) {
            c.setStatus(StatusAtuador.LIGADO);
            c.setLigadoEm(LocalDateTime.now());
        }
        return toDTO(climatizacaoRepository.save(c));
    }

    @Transactional
    public void deletar(Long id) {
        buscarEntidade(id);
        climatizacaoRepository.deleteById(id);
    }

    public Double getConsumoAtivo() {
        Double total = climatizacaoRepository.sumConsumoAtivo();
        return total != null ? total : 0.0;
    }

    public long contarLigados() {
        return climatizacaoRepository.findByStatus(StatusAtuador.LIGADO).size();
    }

    public long contarFalhas() {
        return climatizacaoRepository.findByStatus(StatusAtuador.FALHA).size();
    }

    private Climatizacao buscarEntidade(Long id) {
        return climatizacaoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sistema de climatização não encontrado com id: " + id));
    }

    private Climatizacao toEntity(ClimatizacaoDTO dto) {
        return Climatizacao.builder()
                .nome(dto.getNome())
                .tipoSistema(dto.getTipoSistema())
                .localizacao(dto.getLocalizacao())
                .intensidadePercentual(dto.getIntensidadePercentual() != null ? dto.getIntensidadePercentual() : 0)
                .status(dto.getStatus() != null ? dto.getStatus() : StatusAtuador.DESLIGADO)
                .temperaturaAlvo(dto.getTemperaturaAlvo())
                .temperaturaAtual(dto.getTemperaturaAtual())
                .umidadeAlvo(dto.getUmidadeAlvo())
                .modoAutomatico(dto.getModoAutomatico() != null ? dto.getModoAutomatico() : false)
                .consumoWatts(dto.getConsumoWatts())
                .build();
    }

    public ClimatizacaoDTO toDTO(Climatizacao c) {
        return ClimatizacaoDTO.builder()
                .id(c.getId())
                .nome(c.getNome())
                .tipoSistema(c.getTipoSistema())
                .localizacao(c.getLocalizacao())
                .intensidadePercentual(c.getIntensidadePercentual())
                .status(c.getStatus())
                .temperaturaAlvo(c.getTemperaturaAlvo())
                .temperaturaAtual(c.getTemperaturaAtual())
                .umidadeAlvo(c.getUmidadeAlvo())
                .modoAutomatico(c.getModoAutomatico())
                .consumoWatts(c.getConsumoWatts())
                .ligadoEm(c.getLigadoEm())
                .desligadoEm(c.getDesligadoEm())
                .criadoEm(c.getCriadoEm())
                .atualizadoEm(c.getAtualizadoEm())
                .build();
    }
}
