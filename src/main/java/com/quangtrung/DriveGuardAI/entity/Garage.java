package com.quangtrung.DriveGuardAI.entity;

import com.quangtrung.DriveGuardAI.entity.CarPart;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "garages")
public class Garage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "garage_id")
    private Long garageId;

    @OneToMany(mappedBy = "garage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarPart> carParts;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String city;
    private String phone;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Double rating;






}
