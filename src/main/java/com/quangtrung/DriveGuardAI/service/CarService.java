package com.quangtrung.DriveGuardAI.service;

import com.quangtrung.DriveGuardAI.dto.car.CarDTO;
import com.quangtrung.DriveGuardAI.entity.Car;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CarService {
    public Car saveCar(CarDTO carDTO, MultipartFile file) throws IOException;
}
