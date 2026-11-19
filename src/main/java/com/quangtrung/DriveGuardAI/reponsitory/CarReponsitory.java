package com.quangtrung.DriveGuardAI.reponsitory;

import com.quangtrung.DriveGuardAI.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarReponsitory extends JpaRepository<Car, Long> {
    boolean existsByCarCode(String carCode);
    boolean existsByCarNumber(String carNumber);
}
