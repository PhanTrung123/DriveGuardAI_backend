package com.quangtrung.DriveGuardAI.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "car_errors")
@Data
public class CarError {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_error_id")
    private Long carErrorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sensor_log_id")
    private SensorLog sensorLog;

    @Column(name = "error_code", nullable = false, length = 50)
    private String errorCode;

    @Column(name = "danger_level", length = 50)
    private String dangerLevel; // LOW, MEDIUM, HIGH, CRITICAL

    @Column(name = "ai_reason", columnDefinition = "TEXT")
    private String aiReason;

    @Column(name = "ai_advice", columnDefinition = "TEXT")
    private String aiAdvice;

    @Column(name = "fix_price", precision = 15, scale = 2)
    private BigDecimal fixPrice;

    @Column(name = "is_fixed")
    private Boolean isFixed = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

}
