package com.quangtrung.DriveGuardAI.reponsitory;

import com.quangtrung.DriveGuardAI.entity.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripReponsitory extends JpaRepository<Trip, Long> {
    Page<Trip> findByStartPointContainingIgnoreCase(String startPoint, Pageable pageable);
}
