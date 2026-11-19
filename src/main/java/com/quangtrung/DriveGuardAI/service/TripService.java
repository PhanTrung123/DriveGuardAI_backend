package com.quangtrung.DriveGuardAI.service;

import com.quangtrung.DriveGuardAI.dto.trip.TripDTO;
import com.quangtrung.DriveGuardAI.dto.trip.TripResponseDTO;
import com.quangtrung.DriveGuardAI.entity.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TripService {
    public Trip saveTrip(TripDTO tripDTO);
    public void updateTrip(TripDTO tripDTO);
    public Page<TripResponseDTO> findByStartPoint(Pageable pageable, String startPoint);
    public Page<TripResponseDTO> findWithPageAble(Pageable pageable);
}
