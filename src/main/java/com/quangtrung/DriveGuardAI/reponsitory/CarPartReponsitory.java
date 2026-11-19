package com.quangtrung.DriveGuardAI.reponsitory;

import com.quangtrung.DriveGuardAI.entity.CarPart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarPartReponsitory extends JpaRepository<CarPart, Long> {
    Page<CarPart> findByPartNameContainingIgnoreCase(String name, Pageable pageable);
}
