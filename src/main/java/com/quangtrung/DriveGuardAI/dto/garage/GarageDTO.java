package com.quangtrung.DriveGuardAI.dto.garage;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GarageDTO {
    private Long garageId;

    @NotBlank(message = "Garage name is required")
    @Size(max = 255, message = "Garage name must not exceed 255 characters")
    private String name;

    @NotBlank(message = "Address is required")
    @Size(max = 500, message = "Address must not exceed 500 characters")
    private String address;

    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City must not exceed 100 characters")
    private String city;

    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    @Pattern(regexp = "^[0-9+\\-\\s()]*$", message = "Phone number must contain only digits, +, -, spaces, and parentheses")
    private String phone;

    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;

    @DecimalMin(value = "0.0", message = "Rating must be at least 0.0")
    @DecimalMax(value = "5.0", message = "Rating must not exceed 5.0")
    private Double rating;
}
