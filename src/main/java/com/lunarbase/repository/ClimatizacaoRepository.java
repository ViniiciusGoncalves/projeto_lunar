package com.lunarbase.repository;

import com.lunarbase.model.Climatizacao;
import com.lunarbase.model.StatusAtuador;
import com.lunarbase.model.TipoSistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClimatizacaoRepository extends JpaRepository<Climatizacao, Long> {

    List<Climatizacao> findByTipoSistema(TipoSistema tipoSistema);

    List<Climatizacao> findByStatus(StatusAtuador status);

    List<Climatizacao> findByLocalizacaoContainingIgnoreCase(String localizacao);

    List<Climatizacao> findByModoAutomaticoTrue();

    List<Climatizacao> findByTipoSistemaAndStatus(TipoSistema tipoSistema, StatusAtuador status);

    @Query("SELECT SUM(c.consumoWatts) FROM Climatizacao c WHERE c.status = 'LIGADO'")
    Double sumConsumoAtivo();
}
