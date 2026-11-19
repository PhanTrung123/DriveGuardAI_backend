package com.quangtrung.DriveGuardAI.reponsitory;

import com.quangtrung.DriveGuardAI.entity.Garage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GarageReponsitory extends JpaRepository<Garage, Long> {
    Page<Garage> findByNameContainingIgnoreCase(String name, Pageable pageable);

}
