package com.lunarbase.repository;

import com.lunarbase.model.Sensor;
import com.lunarbase.model.StatusSensor;
import com.lunarbase.model.TipoSensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {

    List<Sensor> findByTipo(TipoSensor tipo);

    List<Sensor> findByStatus(StatusSensor status);

    List<Sensor> findByLocalizacaoContainingIgnoreCase(String localizacao);

    List<Sensor> findByTipoAndStatus(TipoSensor tipo, StatusSensor status);

    @Query("SELECT s FROM Sensor s WHERE s.valorLeitura < s.limiteMinimo AND s.limiteMinimo IS NOT NULL")
    List<Sensor> findSensoresAbaixoLimiteMinimo();

    @Query("SELECT s FROM Sensor s WHERE s.valorLeitura > s.limiteMaximo AND s.limiteMaximo IS NOT NULL")
    List<Sensor> findSensoresAcimaLimiteMaximo();

    boolean existsByNomeAndLocalizacao(String nome, String localizacao);
}
