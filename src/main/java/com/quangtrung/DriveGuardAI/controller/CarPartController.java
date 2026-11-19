package com.quangtrung.DriveGuardAI.controller;

import com.quangtrung.DriveGuardAI.dto.car.CarPartDTO;
import com.quangtrung.DriveGuardAI.entity.CarPart;
import com.quangtrung.DriveGuardAI.service.CarPartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/car-part")
public class CarPartController {

    @Autowired
    private CarPartService carPartService;

    @PostMapping("/save")
    public ResponseEntity<String> saveCarPart(@Valid @RequestBody CarPartDTO carPartDTO) {
        try{
            carPartService.saveCarPart(carPartDTO);
            return ResponseEntity.ok("Lưu phụ tùng thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi lưu phụ tùng: " + e.getMessage());
        }

    }

    @GetMapping("/search")
    public Page<CarPart> findListCarPartByName(@RequestParam(name = "name", required = false) String name, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CarPart> carPartList;
        if(name != null && !name.trim().isEmpty()) {
            carPartList = carPartService.findByName(pageable, name);
        } else {
            carPartList = carPartService.findWithPageAble(pageable);
        }
        return carPartList;
    }
}
