package com.quangtrung.DriveGuardAI.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "car_parts")
@Data
public class CarPart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_part_id")
    private Long carPartId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "garage_id", nullable = false)
    @JsonIgnore
    private Garage garage;

    @Column(name = "part_name", nullable = false)
    private String partName;

    @Column(name = "part_code", nullable = false, length = 100)
    private String partCode;

    @Column(name = "car_brand", length = 100)
    private String carBrand;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal price;

    @Column(name = "for_error_codes")
    private String forErrorCodes;
}
