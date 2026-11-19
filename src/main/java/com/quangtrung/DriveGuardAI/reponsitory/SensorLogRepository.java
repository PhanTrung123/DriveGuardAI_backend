package com.quangtrung.DriveGuardAI.reponsitory;

import com.quangtrung.DriveGuardAI.entity.SensorLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SensorLogRepository extends JpaRepository<SensorLog, Long> {
    long countByTrip_TripId(Long tripId);

    Optional<SensorLog> findFirstByTrip_TripIdOrderByCreatedAtDesc(Long tripId);
}
