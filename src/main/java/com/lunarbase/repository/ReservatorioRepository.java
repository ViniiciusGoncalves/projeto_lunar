package com.lunarbase.repository;

import com.lunarbase.model.Reservatorio;
import com.lunarbase.model.StatusReservatorio;
import com.lunarbase.model.TipoLiquido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservatorioRepository extends JpaRepository<Reservatorio, Long> {

    List<Reservatorio> findByStatus(StatusReservatorio status);

    List<Reservatorio> findByTipoLiquido(TipoLiquido tipoLiquido);

    @Query("SELECT r FROM Reservatorio r WHERE (r.nivelAtual / r.capacidadeMaxima * 100) <= r.alertaNivelMinimo")
    List<Reservatorio> findReservatoriosNivelBaixo();

    @Query("SELECT r FROM Reservatorio r WHERE (r.nivelAtual / r.capacidadeMaxima * 100) >= r.alertaNivelMaximo")
    List<Reservatorio> findReservatoriosQuaseOverflow();

    @Query("SELECT SUM(r.nivelAtual) FROM Reservatorio r WHERE r.tipoLiquido = :tipo")
    Double sumNivelAtualByTipoLiquido(TipoLiquido tipo);
}
