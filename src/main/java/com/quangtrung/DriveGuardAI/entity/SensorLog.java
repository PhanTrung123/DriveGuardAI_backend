package com.quangtrung.DriveGuardAI.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "sensor_logs")
@Data
public class SensorLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sensor_log_id")
    private Long sensorLogId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    private Double speed;

    private Integer rpm; // số vòng mỗi phút

    @Column(name = "engine_temp")
    private Double engineTemp;

    @Column(name = "fuel_percent")
    private Double fuelPercent;

    @Column(name = "battery_volt")
    private Double batteryVolt;

    @Column(name = "current_location")
    private String currentLocation; // Lưu Kinh độ, Vĩ độ (Lat, Lng)

    @Column(name = "error_code", length = 50)
    private String errorCode;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
