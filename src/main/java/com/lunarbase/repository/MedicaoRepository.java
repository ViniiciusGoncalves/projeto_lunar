package com.lunarbase.repository;

import com.lunarbase.model.Medicao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicaoRepository extends JpaRepository<Medicao, Long> {
    List<Medicao> findBySensorId(Long sensorId);
}