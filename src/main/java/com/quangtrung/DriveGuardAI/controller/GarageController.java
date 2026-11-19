package com.quangtrung.DriveGuardAI.controller;

import com.quangtrung.DriveGuardAI.dto.garage.GarageDTO;
import com.quangtrung.DriveGuardAI.entity.Garage;
import com.quangtrung.DriveGuardAI.service.GarageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/garage")
public class GarageController {

    @Autowired
    private GarageService garageService;

    @PostMapping("/save")
    public ResponseEntity<String> saveGarage(@Valid @RequestBody GarageDTO garageDTO) {
        try{
            garageService.saveGarage(garageDTO);
            return ResponseEntity.ok("Lưu garage thành công!");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi lưu garage: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public Page<Garage> findListGarageByName(@RequestParam(name = "name", required = false) String name, @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "5") int size
                                             ){
        Pageable pageable = PageRequest.of(page, size);
        Page<Garage> garageList;
        if(name != null && !name.trim().isEmpty()) {
            garageList = garageService.findByName(pageable, name);
        } else {
            garageList = garageService.findWithPageAble(pageable);
        }
        return garageList;
    }

    @DeleteMapping("/delete/{garageId}")
    public ResponseEntity<?> deleteGarage(@PathVariable("garageId") Long garageId) {
        try{
            Garage garage = garageService.findById(garageId);
            String garageName = garage.getName();
            garageService.deleteGarage(garageId);
            return ResponseEntity.ok("Xóa garage '" + garageName + "' (ID: " + garageId + ") thành công!");
        }catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xóa garage: " + e.getMessage());
        }
    }

}
