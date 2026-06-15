package com.lunarbase.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "medicoes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medicao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sensor_id", nullable = false)
    private Sensor sensor;

    @Column(nullable = false)
    private Double valor;

    @Column(nullable = false)
    private LocalDateTime data;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusMedicao status;
}