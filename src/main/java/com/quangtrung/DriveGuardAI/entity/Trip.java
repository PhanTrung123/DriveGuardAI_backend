package com.quangtrung.DriveGuardAI.entity;

import com.quangtrung.DriveGuardAI.enums.TripStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "trips")
@Data
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_id")
    private Long tripId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "start_point")
    private String startPoint;

    @Column(name = "end_point")
    private String endPoint;

    @Column(name = "total_km")
    private Double totalKm;

    @Column(name = "max_speed")
    private Double maxSpeed;

    @Column(name = "safe_score")
    private Integer safeScore;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TripStatus status = TripStatus.ACTIVE; // Mặc định khi tạo là ACTIVE

    @Column(name = "route_polyline", columnDefinition = "TEXT")
    private String routePolyline; // chuỗi mã hóa đường đi của Google Maps
}
