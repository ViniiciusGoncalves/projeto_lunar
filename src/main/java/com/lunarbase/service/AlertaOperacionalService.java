package com.lunarbase.service;

import com.lunarbase.dto.AlertaOperacionalDTO;
import com.lunarbase.exception.RecursoNaoEncontradoException;
import com.lunarbase.model.*;
import com.lunarbase.repository.AlertaOperacionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlertaOperacionalService {

    private final AlertaOperacionalRepository alertaRepository;

    @Transactional
    public AlertaOperacionalDTO criar(AlertaOperacionalDTO dto) {
        AlertaOperacional alerta = toEntity(dto);
        return toDTO(alertaRepository.save(alerta));
    }

    public List<AlertaOperacionalDTO> listarTodos() {
        return alertaRepository.findAll()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public AlertaOperacionalDTO buscarPorId(Long id) {
        return toDTO(buscarEntidade(id));
    }

    public List<AlertaOperacionalDTO> listarAtivos() {
        return alertaRepository.findAlertasAtivosOrdenadosPorSeveridade()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<AlertaOperacionalDTO> listarPorStatus(StatusAlerta status) {
        return alertaRepository.findByStatusOrderByCriadoEmDesc(status)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<AlertaOperacionalDTO> listarPorSeveridade(SeveridadeAlerta severidade) {
        return alertaRepository.findBySeveridade(severidade)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<AlertaOperacionalDTO> listarPorTipo(TipoAlerta tipo) {
        return alertaRepository.findByTipoAlerta(tipo)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<AlertaOperacionalDTO> listarPorSetor(String setor) {
        return alertaRepository.findBySetorIgnoreCase(setor)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public AlertaOperacionalDTO atualizar(Long id, AlertaOperacionalDTO dto) {
        AlertaOperacional alerta = buscarEntidade(id);
        alerta.setTitulo(dto.getTitulo());
        alerta.setDescricao(dto.getDescricao());
        alerta.setSeveridade(dto.getSeveridade());
        alerta.setTipoAlerta(dto.getTipoAlerta());
        alerta.setTipoNotificacao(dto.getTipoNotificacao());
        alerta.setObservacoes(dto.getObservacoes());
        alerta.setSetor(dto.getSetor());
        if (dto.getStatus() != null) alerta.setStatus(dto.getStatus());
        return toDTO(alertaRepository.save(alerta));
    }

    @Transactional
    public AlertaOperacionalDTO reconhecer(Long id, String operador) {
        AlertaOperacional alerta = buscarEntidade(id);
        alerta.setStatus(StatusAlerta.RECONHECIDO);
        alerta.setReconhecidoPor(operador);
        alerta.setReconhecidoEm(LocalDateTime.now());
        return toDTO(alertaRepository.save(alerta));
    }

    @Transactional
    public AlertaOperacionalDTO resolver(Long id, String observacoes) {
        AlertaOperacional alerta = buscarEntidade(id);
        alerta.setStatus(StatusAlerta.RESOLVIDO);
        alerta.setResolvidoEm(LocalDateTime.now());
        if (observacoes != null) alerta.setObservacoes(observacoes);
        return toDTO(alertaRepository.save(alerta));
    }

    @Transactional
    public void deletar(Long id) {
        buscarEntidade(id);
        alertaRepository.deleteById(id);
    }

    // Geração automática de alerta a partir de leitura de sensor
    @Transactional
    public void gerarAlertaAutomatico(Sensor sensor, double valorLido, double limite, boolean acima) {
        TipoAlerta tipo = resolverTipoAlertaSensor(sensor.getTipo(), acima);
        SeveridadeAlerta severidade = calcularSeveridade(sensor.getTipo(), valorLido, limite, acima);

        AlertaOperacional alerta = AlertaOperacional.builder()
                .titulo(String.format("Sensor %s — %s limite", sensor.getNome(), acima ? "acima do" : "abaixo do"))
                .descricao(String.format("Sensor '%s' (%s) em '%s' registrou %.2f. Limite configurado: %.2f",
                        sensor.getNome(), sensor.getTipo(), sensor.getLocalizacao(), valorLido, limite))
                .severidade(severidade)
                .tipoAlerta(tipo)
                .tipoNotificacao(TipoNotificacao.VISUAL_E_SONORO)
                .status(StatusAlerta.ATIVO)
                .recursoId(sensor.getId())
                .recursoNome(sensor.getNome())
                .valorDisparador(valorLido)
                .valorLimite(limite)
                .setor(sensor.getLocalizacao())
                .build();

        alertaRepository.save(alerta);
    }

    private TipoAlerta resolverTipoAlertaSensor(TipoSensor tipo, boolean acima) {
        return switch (tipo) {
            case TEMPERATURA -> acima ? TipoAlerta.TEMPERATURA_ALTA : TipoAlerta.TEMPERATURA_BAIXA;
            case UMIDADE     -> acima ? TipoAlerta.UMIDADE_ALTA     : TipoAlerta.UMIDADE_BAIXA;
            case NIVEL_AGUA  -> acima ? TipoAlerta.OVERFLOW_RESERVATORIO : TipoAlerta.NIVEL_AGUA_CRITICO;
            case CONSUMO_ENERGIA -> TipoAlerta.CONSUMO_ENERGIA_ALTO;
            case NIVEL_BATERIA   -> TipoAlerta.BATERIA_BAIXA;
            case RADIACAO_SOLAR  -> TipoAlerta.RADIACAO_ALTA;
            case QUALIDADE_AR    -> TipoAlerta.QUALIDADE_AR_CRITICA;
            case PRESSAO_ATMOSFERICA -> TipoAlerta.PRESSAO_CRITICA;
            default -> TipoAlerta.FALHA_SENSOR;
        };
    }

    private SeveridadeAlerta calcularSeveridade(TipoSensor tipo, double valor, double limite, boolean acima) {
        double desvio = acima ? (valor - limite) / limite : (limite - valor) / limite;
        if (desvio > 0.30) return SeveridadeAlerta.EMERGENCIA;
        if (desvio > 0.15) return SeveridadeAlerta.CRITICO;
        if (desvio > 0.05) return SeveridadeAlerta.AVISO;
        return SeveridadeAlerta.INFO;
    }

    private AlertaOperacional buscarEntidade(Long id) {
        return alertaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Alerta não encontrado com id: " + id));
    }

    private AlertaOperacional toEntity(AlertaOperacionalDTO dto) {
        return AlertaOperacional.builder()
                .titulo(dto.getTitulo())
                .descricao(dto.getDescricao())
                .severidade(dto.getSeveridade())
                .tipoAlerta(dto.getTipoAlerta())
                .tipoNotificacao(dto.getTipoNotificacao() != null ? dto.getTipoNotificacao() : TipoNotificacao.VISUAL)
                .status(dto.getStatus() != null ? dto.getStatus() : StatusAlerta.ATIVO)
                .recursoId(dto.getRecursoId())
                .recursoNome(dto.getRecursoNome())
                .valorDisparador(dto.getValorDisparador())
                .valorLimite(dto.getValorLimite())
                .setor(dto.getSetor())
                .observacoes(dto.getObservacoes())
                .build();
    }

    public AlertaOperacionalDTO toDTO(AlertaOperacional a) {
        return AlertaOperacionalDTO.builder()
                .id(a.getId())
                .titulo(a.getTitulo())
                .descricao(a.getDescricao())
                .severidade(a.getSeveridade())
                .tipoAlerta(a.getTipoAlerta())
                .tipoNotificacao(a.getTipoNotificacao())
                .status(a.getStatus())
                .recursoId(a.getRecursoId())
                .recursoNome(a.getRecursoNome())
                .valorDisparador(a.getValorDisparador())
                .valorLimite(a.getValorLimite())
                .setor(a.getSetor())
                .reconhecidoPor(a.getReconhecidoPor())
                .reconhecidoEm(a.getReconhecidoEm())
                .resolvidoEm(a.getResolvidoEm())
                .observacoes(a.getObservacoes())
                .criadoEm(a.getCriadoEm())
                .atualizadoEm(a.getAtualizadoEm())
                .build();
    }

    public long contarAtivos() {
        return alertaRepository.countByStatus(StatusAlerta.ATIVO);
    }

    public long contarPorSeveridadeAtivos(SeveridadeAlerta severidade) {
        return alertaRepository.countBySeveridadeAndStatus(severidade, StatusAlerta.ATIVO);
    }
}
