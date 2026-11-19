package com.quangtrung.DriveGuardAI.dto.driver;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class DriverProfileDTO {

    private Long carId;

    @NotNull(message = "ID người dùng không được để trống")
    private Long driverId;

    @NotBlank(message = "Số giấy phép lái xe không được để trống")
    @Size(max = 50, message = "Số giấy phép lái xe không được vượt quá 50 ký tự")
    private String licenseNumber;

    @NotBlank(message = "Loại giấy phép lái xe không được để trống")
    @Size(max = 50, message = "Loại giấy phép lái xe không được vượt quá 50 ký tự")
    private String licenseType;

    @NotNull(message = "Ngày cấp không được để trống")
    @Past(message = "Ngày cấp phải là ngày trong quá khứ")
    private LocalDate issueDate;

    @NotNull(message = "Ngày hết hạn không được để trống")
    private LocalDate expiryDate;

    private Integer experienceYears;

    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;

    @NotBlank(message = "Số CMND/CCCD không được để trống")
    @Size(max = 20, message = "Số CMND/CCCD không được vượt quá 20 ký tự")
    private String idCardNumber;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Size(max = 15, message = "Số điện thoại không được vượt quá 15 ký tự")
    private String phone;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean isActive;
}
