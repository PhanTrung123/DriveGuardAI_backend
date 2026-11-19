package com.quangtrung.DriveGuardAI.controller;

import com.quangtrung.DriveGuardAI.dto.car.CarDTO;
import com.quangtrung.DriveGuardAI.entity.Car;
import com.quangtrung.DriveGuardAI.service.CarService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/car")
public class CarController {

    @Autowired
    private CarService carService;

    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> saveCar(
            @Valid @RequestPart("carDTO") CarDTO carDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            Car savedCar = carService.saveCar(carDTO, file);
            return ResponseEntity.ok("Lưu thông tin xe thành công!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lỗi lưu xe: " + e.getMessage());
        }
    }
}
