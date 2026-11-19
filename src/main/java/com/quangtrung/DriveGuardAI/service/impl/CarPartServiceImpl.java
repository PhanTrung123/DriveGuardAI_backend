package com.quangtrung.DriveGuardAI.service.impl;

import com.quangtrung.DriveGuardAI.dto.car.CarPartDTO;
import com.quangtrung.DriveGuardAI.entity.CarPart;
import com.quangtrung.DriveGuardAI.entity.Garage;
import com.quangtrung.DriveGuardAI.reponsitory.CarPartReponsitory;
import com.quangtrung.DriveGuardAI.reponsitory.GarageReponsitory;
import com.quangtrung.DriveGuardAI.service.CarPartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CarPartServiceImpl implements CarPartService {
    @Autowired
    private CarPartReponsitory carPartReponsitory;

    @Autowired
    private GarageReponsitory garageReponsitory;

    @Override
    public void saveCarPart(CarPartDTO carPartDTO) {
        CarPart carPart;

        if (carPartDTO.getCarPartId() != null) {
            carPart = carPartReponsitory.findById(carPartDTO.getCarPartId())
                    .orElseThrow(() -> new RuntimeException("CarPart not found with id: " + carPartDTO.getCarPartId()));
        } else {
            carPart = new CarPart();
        }

        Garage garage = garageReponsitory.findById(carPartDTO.getGarageId())
                .orElseThrow(() -> new RuntimeException("Garage not found with id: " + carPartDTO.getGarageId()));
        carPart.setGarage(garage);

        carPart.setPartName(carPartDTO.getPartName());
        carPart.setPartCode(carPartDTO.getPartCode());
        carPart.setCarBrand(carPartDTO.getCarBrand());
        carPart.setQuantity(carPartDTO.getQuantity());
        carPart.setPrice(carPartDTO.getPrice());
        carPart.setForErrorCodes(carPartDTO.getForErrorCodes());

        carPartReponsitory.save(carPart);
    }

    @Override
    public Page<CarPart> findByName(Pageable pageable, String name) {
        return carPartReponsitory.findByPartNameContainingIgnoreCase(name, pageable);
    }

    @Override
    public Page<CarPart> findWithPageAble(Pageable pageable) {
        return carPartReponsitory.findAll(pageable);
    }
}
