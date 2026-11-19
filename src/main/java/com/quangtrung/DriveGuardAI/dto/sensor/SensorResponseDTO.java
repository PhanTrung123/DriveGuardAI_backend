package com.quangtrung.DriveGuardAI.dto.sensor;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SensorResponseDTO {
    private String message;
    private String currentLocation;
    private Double currentSpeed;
    private String errorCode;
    private String aiAdvice;
    private String nearestGarageInfo;
}
