package com.lunarbase.repository;

import com.lunarbase.model.ConsumoEnergia;
import com.lunarbase.model.StatusEnergia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConsumoEnergiaRepository extends JpaRepository<ConsumoEnergia, Long> {

    List<ConsumoEnergia> findByStatus(StatusEnergia status);

    Optional<ConsumoEnergia> findBySetor(String setor);

    List<ConsumoEnergia> findBySetorContainingIgnoreCase(String setor);

    @Query("SELECT SUM(c.consumoKwh) FROM ConsumoEnergia c")
    Double sumConsumoTotal();

    @Query("SELECT SUM(c.geracaoSolarKwh) FROM ConsumoEnergia c")
    Double sumGeracaoTotal();

    @Query("SELECT c FROM ConsumoEnergia c WHERE c.consumoKwh > c.limiteConsumoAlerta AND c.limiteConsumoAlerta IS NOT NULL")
    List<ConsumoEnergia> findSetoresAcimaLimite();

    @Query("SELECT c FROM ConsumoEnergia c WHERE c.nivelBateriaPercentual < 20")
    List<ConsumoEnergia> findSetoresBateriaCritica();
}
