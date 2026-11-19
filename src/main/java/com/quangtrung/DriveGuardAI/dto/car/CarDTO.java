package com.quangtrung.DriveGuardAI.dto.car;

import com.quangtrung.DriveGuardAI.enums.CarStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CarDTO {

    @NotBlank(message = "Mã xe không được để trống")
    @Size(max = 50, message = "Mã xe không được vượt quá 50 ký tự")
    private String carCode;

    @NotBlank(message = "Biển số xe không được để trống")
    @Size(max = 30, message = "Biển số xe không được vượt quá 30 ký tự")
    private String carNumber;

    @NotBlank(message = "Hãng xe không được để trống")
    private String brand;

    @NotBlank(message = "Mẫu xe không được để trống")
    private String model;

    @Min(value = 1900, message = "Năm sản xuất không được nhỏ hơn 1900")
    @Max(value = 2100, message = "Năm sản xuất không được lớn hơn 2100")
    private Integer year;

    @NotNull(message = "Trạng thái xe không được để trống")
    private CarStatus status;

    private String imgUrl;

    private Double pricePerKm;
}
