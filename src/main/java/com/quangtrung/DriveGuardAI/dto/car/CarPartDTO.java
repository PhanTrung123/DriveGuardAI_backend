package com.quangtrung.DriveGuardAI.dto.car;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarPartDTO {
    private Long carPartId;

    @NotNull(message = "Garage ID is required")
    private Long garageId;

    @NotBlank(message = "Part name is required")
    @Size(max = 255, message = "Part name must not exceed 255 characters")
    private String partName;

    @NotBlank(message = "Part code is required")
    @Size(max = 100, message = "Part code must not exceed 100 characters")
    private String partCode;

    @Size(max = 100, message = "Car brand must not exceed 100 characters")
    private String carBrand;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity must be at least 0")
    private Integer quantity;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @Digits(integer = 13, fraction = 2, message = "Price must have at most 13 integer digits and 2 decimal digits")
    private BigDecimal price;

    private String forErrorCodes;
}
