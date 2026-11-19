package com.quangtrung.DriveGuardAI.service;

import com.quangtrung.DriveGuardAI.dto.car.CarPartDTO;
import com.quangtrung.DriveGuardAI.entity.CarPart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CarPartService {
    public void saveCarPart(CarPartDTO carPartDTO);

    Page<CarPart> findByName(Pageable pageable, String name);

    Page<CarPart> findWithPageAble(Pageable pageable);
}
