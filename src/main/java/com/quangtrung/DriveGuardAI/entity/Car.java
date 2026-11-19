package com.quangtrung.DriveGuardAI.entity;

import com.quangtrung.DriveGuardAI.enums.CarStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "cars")
@Data
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_id")
    private Long carId;

    @Column(name = "car_code", unique = true, nullable = false, length = 50)
    private String carCode;

    @Column(name = "car_number", unique = true, nullable = false, length = 30)
    private String carNumber;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @Column(name = "year")
    private Integer year;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private CarStatus status;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "price_per_km")
    private Double pricePerKm;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;






}
