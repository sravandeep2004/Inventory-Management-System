package com.myapp.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating inventory quantity (PATCH endpoint)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateQuantityDto {

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;
}
