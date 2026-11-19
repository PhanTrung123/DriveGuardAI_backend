package com.quangtrung.DriveGuardAI.controller;

import com.quangtrung.DriveGuardAI.dto.driver.DriverProfileDTO;
import com.quangtrung.DriveGuardAI.service.DriverProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/driver-profile")
public class DriverProfileController {

    @Autowired
    private DriverProfileService driverProfileService;

    @PostMapping("/save")
    public ResponseEntity<?> saveDriverProfile(@Valid @RequestBody DriverProfileDTO driverProfileDTO) {
        try {
            String message = driverProfileService.saveDriverProfile(driverProfileDTO);
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lỗi lưu hồ sơ tài xế: " + e.getMessage());
        }
    }
}
