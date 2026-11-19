package com.quangtrung.DriveGuardAI.service.impl;

import com.quangtrung.DriveGuardAI.dto.car.CarDTO;
import com.quangtrung.DriveGuardAI.entity.Car;
import com.quangtrung.DriveGuardAI.reponsitory.CarReponsitory;
import com.quangtrung.DriveGuardAI.service.CarService;
import com.quangtrung.DriveGuardAI.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class CarServiceImpl implements CarService {

    @Autowired
    private CarReponsitory carReponsitory;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public Car saveCar(CarDTO carDTO, MultipartFile file) throws IOException {
        if (carReponsitory.existsByCarCode(carDTO.getCarCode())) {
            throw new IllegalArgumentException("Mã xe " + carDTO.getCarCode() + " đã tồn tại!");
        }
        if (carReponsitory.existsByCarNumber(carDTO.getCarNumber())) {
            throw new IllegalArgumentException("Biển số xe " + carDTO.getCarNumber() + " đã tồn tại!");
        }

        Car car = new Car();
        car.setCarCode(carDTO.getCarCode());
        car.setCarNumber(carDTO.getCarNumber());
        car.setBrand(carDTO.getBrand());
        car.setModel(carDTO.getModel());
        car.setYear(carDTO.getYear());
        car.setStatus(carDTO.getStatus());
        car.setPricePerKm(carDTO.getPricePerKm());

        // Upload file lên Cloudinary nếu có file
        if (file != null && !file.isEmpty()) {
            String imgUrl = cloudinaryService.uploadFile(file);
            car.setImgUrl(imgUrl);
        } else {
            car.setImgUrl(carDTO.getImgUrl());
        }

        return carReponsitory.save(car);
    }
}
