package com.lunarbase.service;

import com.lunarbase.dto.ReservatorioDTO;
import com.lunarbase.exception.RecursoNaoEncontradoException;
import com.lunarbase.model.*;
import com.lunarbase.repository.ReservatorioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservatorioService {

    private final ReservatorioRepository reservatorioRepository;

    @Transactional
    public ReservatorioDTO criar(ReservatorioDTO dto) {
        Reservatorio r = toEntity(dto);
        return toDTO(reservatorioRepository.save(r));
    }

    public List<ReservatorioDTO> listarTodos() {
        return reservatorioRepository.findAll()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ReservatorioDTO buscarPorId(Long id) {
        return toDTO(buscarEntidade(id));
    }

    public List<ReservatorioDTO> listarPorStatus(StatusReservatorio status) {
        return reservatorioRepository.findByStatus(status)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<ReservatorioDTO> listarNivelBaixo() {
        return reservatorioRepository.findReservatoriosNivelBaixo()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public ReservatorioDTO atualizar(Long id, ReservatorioDTO dto) {
        Reservatorio r = buscarEntidade(id);
        r.setNome(dto.getNome());
        r.setCapacidadeMaxima(dto.getCapacidadeMaxima());
        r.setLocalizacao(dto.getLocalizacao());
        r.setTipoLiquido(dto.getTipoLiquido() != null ? dto.getTipoLiquido() : r.getTipoLiquido());
        r.setAlertaNivelMinimo(dto.getAlertaNivelMinimo() != null ? dto.getAlertaNivelMinimo() : r.getAlertaNivelMinimo());
        r.setAlertaNivelMaximo(dto.getAlertaNivelMaximo() != null ? dto.getAlertaNivelMaximo() : r.getAlertaNivelMaximo());
        if (dto.getStatus() != null) r.setStatus(dto.getStatus());
        return toDTO(reservatorioRepository.save(r));
    }

    @Transactional
    public ReservatorioDTO atualizarNivel(Long id, Double novoNivel) {
        Reservatorio r = buscarEntidade(id);
        if (novoNivel < 0) throw new IllegalArgumentException("Nível não pode ser negativo");
        if (novoNivel > r.getCapacidadeMaxima())
            throw new IllegalArgumentException("Nível excede a capacidade máxima do reservatório");

        r.setNivelAtual(novoNivel);
        r.setUltimaRecarga(LocalDateTime.now());

        // Atualiza status conforme nível
        double pct = (novoNivel / r.getCapacidadeMaxima()) * 100;
        if (pct <= 5)                         r.setStatus(StatusReservatorio.NIVEL_CRITICO);
        else if (pct <= r.getAlertaNivelMinimo()) r.setStatus(StatusReservatorio.NIVEL_BAIXO);
        else if (pct >= r.getAlertaNivelMaximo()) r.setStatus(StatusReservatorio.OVERFLOW);
        else                                  r.setStatus(StatusReservatorio.NORMAL);

        return toDTO(reservatorioRepository.save(r));
    }

    @Transactional
    public void deletar(Long id) {
        buscarEntidade(id);
        reservatorioRepository.deleteById(id);
    }

    private Reservatorio buscarEntidade(Long id) {
        return reservatorioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Reservatório não encontrado com id: " + id));
    }

    private Reservatorio toEntity(ReservatorioDTO dto) {
        return Reservatorio.builder()
                .nome(dto.getNome())
                .capacidadeMaxima(dto.getCapacidadeMaxima())
                .nivelAtual(dto.getNivelAtual() != null ? dto.getNivelAtual() : 0.0)
                .localizacao(dto.getLocalizacao())
                .tipoLiquido(dto.getTipoLiquido() != null ? dto.getTipoLiquido() : TipoLiquido.AGUA_POTAVEL)
                .status(dto.getStatus() != null ? dto.getStatus() : StatusReservatorio.NORMAL)
                .alertaNivelMinimo(dto.getAlertaNivelMinimo() != null ? dto.getAlertaNivelMinimo() : 20.0)
                .alertaNivelMaximo(dto.getAlertaNivelMaximo() != null ? dto.getAlertaNivelMaximo() : 95.0)
                .ultimaRecarga(dto.getUltimaRecarga())
                .build();
    }

    public ReservatorioDTO toDTO(Reservatorio r) {
        return ReservatorioDTO.builder()
                .id(r.getId())
                .nome(r.getNome())
                .capacidadeMaxima(r.getCapacidadeMaxima())
                .nivelAtual(r.getNivelAtual())
                .nivelPercentual(r.getNivelPercentual())
                .localizacao(r.getLocalizacao())
                .tipoLiquido(r.getTipoLiquido())
                .status(r.getStatus())
                .alertaNivelMinimo(r.getAlertaNivelMinimo())
                .alertaNivelMaximo(r.getAlertaNivelMaximo())
                .ultimaRecarga(r.getUltimaRecarga())
                .criadoEm(r.getCriadoEm())
                .atualizadoEm(r.getAtualizadoEm())
                .build();
    }
}
