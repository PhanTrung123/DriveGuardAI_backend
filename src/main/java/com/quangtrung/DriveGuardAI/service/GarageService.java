package com.quangtrung.DriveGuardAI.service;

import com.quangtrung.DriveGuardAI.dto.garage.GarageDTO;
import com.quangtrung.DriveGuardAI.entity.Garage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GarageService {
    public void saveGarage(GarageDTO garageDTO);

    Page<Garage> findByName(Pageable pageable, String name);

    Page<Garage> findWithPageAble(Pageable pageable);

    Garage findById(Long garageId);

    void deleteGarage(Long garageId);
}
