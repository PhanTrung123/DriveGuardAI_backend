package com.quangtrung.DriveGuardAI.reponsitory;

import com.quangtrung.DriveGuardAI.entity.CarDriver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DriverProfileRepository extends JpaRepository<CarDriver, Long> {
    @Query("SELECT CASE WHEN COUNT(cd) > 0 THEN true ELSE false END FROM CarDriver cd WHERE cd.driver.userId = :userId")
    boolean existsByUserId(@Param("userId") Long userId);

    boolean existsByLicenseNumber(String licenseNumber);
    boolean existsByIdCardNumber(String idCardNumber);
}
