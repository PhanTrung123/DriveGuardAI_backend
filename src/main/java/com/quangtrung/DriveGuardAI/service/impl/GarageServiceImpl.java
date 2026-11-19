package com.quangtrung.DriveGuardAI.service.impl;

import com.quangtrung.DriveGuardAI.dto.garage.GarageDTO;
import com.quangtrung.DriveGuardAI.entity.Garage;
import com.quangtrung.DriveGuardAI.reponsitory.GarageReponsitory;
import com.quangtrung.DriveGuardAI.service.GarageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GarageServiceImpl implements GarageService {
    @Autowired
    private GarageReponsitory garageReponsitory;

    @Override
    public void saveGarage(GarageDTO garageDTO) {
        Garage garage;
        if (garageDTO.getGarageId() != null) {
            garage = garageReponsitory.findById(garageDTO.getGarageId())
                    .orElseThrow(() -> new RuntimeException("Garage not found with id: " + garageDTO.getGarageId()));
        } else {
            garage = new Garage();
        }
        garage.setName(garageDTO.getName());
        garage.setAddress(garageDTO.getAddress());
        garage.setCity(garageDTO.getCity());
        garage.setPhone(garageDTO.getPhone());
        garage.setDescription(garageDTO.getDescription());
        garage.setRating(garageDTO.getRating());
        garageReponsitory.save(garage);
    }

    @Override
    public Page<Garage> findByName(Pageable pageable, String name) {
        return garageReponsitory.findByNameContainingIgnoreCase(name, pageable);
    }

    @Override
    public Page<Garage> findWithPageAble(Pageable pageable) {
        return garageReponsitory.findAll(pageable);
    }

    @Override
    public Garage findById(Long garageId) {
        return garageReponsitory.findById(garageId)
                .orElseThrow(() -> new RuntimeException("Garage not found with id: " + garageId));
    }

    @Override
    public void deleteGarage(Long garageId) {
        Garage garage = findById(garageId);
        garageReponsitory.delete(garage);
    }
}
