package com.quangtrung.DriveGuardAI.controller;

import com.quangtrung.DriveGuardAI.dto.trip.TripDTO;
import com.quangtrung.DriveGuardAI.dto.trip.TripResponseDTO;
import com.quangtrung.DriveGuardAI.entity.Trip;
import com.quangtrung.DriveGuardAI.service.TripService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trip")
public class TripController {
    @Autowired
    TripService tripService;

    @PostMapping("/save")
    public ResponseEntity<?> saveTrip(@Valid @RequestBody TripDTO tripDTO) {
        try{
            Trip savedTrip = tripService.saveTrip(tripDTO);
            return ResponseEntity.ok("Lưu thông tin chuyến đi thành công!");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lỗi lưu thông tin chuyến đi: " + e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateTrip(@RequestBody TripDTO tripDTO) {
        try{
            tripService.updateTrip(tripDTO);
            return ResponseEntity.ok("Cập nhật thông tin chuyến đi thành công!");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lỗi cập nhật thông tin chuyến đi: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public Page<TripResponseDTO> findListTrip(@RequestParam(name = "startPoint", required = false) String startPoint,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TripResponseDTO> tripList;
        if (startPoint != null && !startPoint.trim().isEmpty()) {
            tripList = tripService.findByStartPoint(pageable, startPoint);
        } else {
            tripList = tripService.findWithPageAble(pageable);
        }
        return tripList;
    }

}
