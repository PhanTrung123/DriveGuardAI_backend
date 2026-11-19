package com.quangtrung.DriveGuardAI.dto.trip;

import com.quangtrung.DriveGuardAI.enums.TripStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TripDTO {
    private Long tripId;

    @NotNull(message = "ID xe không được để trống")
    private Long carId;

    @NotNull(message = "ID người dùng không được để trống")
    private Long userId;

    private String userName;

    @NotNull(message = "Thời gian bắt đầu không được để trống")
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @NotBlank(message = "Điểm xuất phát không được để trống")
    private String startPoint;

    @NotBlank(message = "Điểm đến không được để trống")
    private String endPoint;

    private Double totalKm;

    private Double maxSpeed;

    private Integer safeScore;

    private TripStatus status;

    private String routePolyline;
}
