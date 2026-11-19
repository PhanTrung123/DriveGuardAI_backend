package com.quangtrung.DriveGuardAI.service;

import com.quangtrung.DriveGuardAI.dto.trip.TripDTO;

public interface SensorLogService {
    public void generateFakeSensorData(TripDTO tripDTO);
}
