package com.lunarbase.repository;

import com.lunarbase.model.AlertaOperacional;
import com.lunarbase.model.SeveridadeAlerta;
import com.lunarbase.model.StatusAlerta;
import com.lunarbase.model.TipoAlerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertaOperacionalRepository extends JpaRepository<AlertaOperacional, Long> {

    List<AlertaOperacional> findByStatus(StatusAlerta status);

    List<AlertaOperacional> findBySeveridade(SeveridadeAlerta severidade);

    List<AlertaOperacional> findByTipoAlerta(TipoAlerta tipoAlerta);

    List<AlertaOperacional> findByStatusOrderByCriadoEmDesc(StatusAlerta status);

    List<AlertaOperacional> findBySeveridadeAndStatus(SeveridadeAlerta severidade, StatusAlerta status);

    List<AlertaOperacional> findBySetorIgnoreCase(String setor);

    // Alertas ativos ordenados por severidade (EMERGENCIA primeiro)
    @Query("SELECT a FROM AlertaOperacional a WHERE a.status = 'ATIVO' " +
           "ORDER BY CASE a.severidade WHEN 'EMERGENCIA' THEN 1 WHEN 'CRITICO' THEN 2 WHEN 'AVISO' THEN 3 ELSE 4 END")
    List<AlertaOperacional> findAlertasAtivosOrdenadosPorSeveridade();

    long countByStatus(StatusAlerta status);

    long countBySeveridadeAndStatus(SeveridadeAlerta severidade, StatusAlerta status);
}
