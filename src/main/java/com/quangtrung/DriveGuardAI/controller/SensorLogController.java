package com.quangtrung.DriveGuardAI.controller;

import com.quangtrung.DriveGuardAI.dto.trip.TripDTO;
import com.quangtrung.DriveGuardAI.service.SensorLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sensor-logs")
public class SensorLogController {

    @Autowired
    private SensorLogService sensorLogService;

    @PostMapping("/save")
    public ResponseEntity<?> sensorLogData(@RequestBody TripDTO tripDTO) {
        try{
            sensorLogService.generateFakeSensorData(tripDTO);
            return ResponseEntity.ok("Đã nhận và ghi log cảm biến thành công");
        }catch(Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }
}
