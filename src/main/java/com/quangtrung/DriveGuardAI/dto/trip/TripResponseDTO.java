package com.quangtrung.DriveGuardAI.dto.trip;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TripResponseDTO {
    private Long tripId;
    private Long carId;
    private Long userId;
    private String fullName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String startPoint;
    private String endPoint;
    private Double totalKm;
    private Double maxSpeed;
    private Integer safeScore;
}
