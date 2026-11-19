package com.quangtrung.DriveGuardAI.service.impl;

import com.quangtrung.DriveGuardAI.dto.driver.DriverProfileDTO;
import com.quangtrung.DriveGuardAI.entity.Car;
import com.quangtrung.DriveGuardAI.entity.CarDriver;
import com.quangtrung.DriveGuardAI.entity.User;
import com.quangtrung.DriveGuardAI.enums.Role;
import com.quangtrung.DriveGuardAI.reponsitory.CarReponsitory;
import com.quangtrung.DriveGuardAI.reponsitory.DriverProfileRepository;
import com.quangtrung.DriveGuardAI.reponsitory.UserReponsitory;
import com.quangtrung.DriveGuardAI.service.DriverProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DriverProfileServiceImpl implements DriverProfileService {

    @Autowired
    private DriverProfileRepository driverProfileRepository;

    @Autowired
    private UserReponsitory userReponsitory;

    @Autowired
    private CarReponsitory carReponsitory;

    @Override
    @Transactional
    public String saveDriverProfile(DriverProfileDTO driverProfileDTO) {
        // Kiểm tra user có tồn tại không
        User user = userReponsitory.findById(driverProfileDTO.getDriverId())
                .orElseThrow(() -> new IllegalArgumentException("Tài khoản với ID " + driverProfileDTO.getDriverId() + " không tồn tại!"));

        // Kiểm tra user có role TÀI_XẾ không
        if (user.getRole() != Role.TÀI_XẾ) {
            throw new IllegalArgumentException("Tài khoản này không có vai trò tài xế!");
        }

        // Kiểm tra user đã có hồ sơ tài xế chưa
        if (driverProfileRepository.existsByUserId(driverProfileDTO.getDriverId())) {
            throw new IllegalArgumentException("Tài khoản này đã có hồ sơ tài xế!");
        }

        // Kiểm tra số giấy phép lái xe có trùng không
        if (driverProfileRepository.existsByLicenseNumber(driverProfileDTO.getLicenseNumber())) {
            throw new IllegalArgumentException("Số giấy phép lái xe " + driverProfileDTO.getLicenseNumber() + " đã tồn tại!");
        }

        // Kiểm tra số CMND/CCCD có trùng không
        if (driverProfileRepository.existsByIdCardNumber(driverProfileDTO.getIdCardNumber())) {
            throw new IllegalArgumentException("Số CMND/CCCD " + driverProfileDTO.getIdCardNumber() + " đã tồn tại!");
        }

        // Tạo hồ sơ tài xế mới
        CarDriver carDriver = new CarDriver();
        carDriver.setDriver(user);
        carDriver.setLicenseNumber(driverProfileDTO.getLicenseNumber());
        carDriver.setLicenseType(driverProfileDTO.getLicenseType());
        carDriver.setIssueDate(driverProfileDTO.getIssueDate());
        carDriver.setExpiryDate(driverProfileDTO.getExpiryDate());
        carDriver.setExperienceYears(driverProfileDTO.getExperienceYears());
        carDriver.setAddress(driverProfileDTO.getAddress());
        carDriver.setIdCardNumber(driverProfileDTO.getIdCardNumber());
        carDriver.setPhone(driverProfileDTO.getPhone());

        // Xử lý carId nếu có
        if (driverProfileDTO.getCarId() != null) {
            Car car = carReponsitory.findById(driverProfileDTO.getCarId())
                    .orElseThrow(() -> new IllegalArgumentException("Xe với ID " + driverProfileDTO.getCarId() + " không tồn tại!"));
            carDriver.setCar(car);
        }

        // Xử lý các trường ngày tháng
        carDriver.setStartDate(driverProfileDTO.getStartDate());
        carDriver.setEndDate(driverProfileDTO.getEndDate());
        carDriver.setIsActive(driverProfileDTO.getIsActive());

        driverProfileRepository.save(carDriver);

        return "Lưu hồ sơ tài xế thành công!";
    }
}
