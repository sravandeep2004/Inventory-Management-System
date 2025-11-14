package com.myapp.demo.dto;

import com.myapp.demo.entity.Status;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for creating InventoryStock
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateInventoryStockDto {

    @NotBlank(message = "Product name is required")
    private String productName;

    @NotNull(message = "Price per unit is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal pricePerUnit;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;

    private Status status;
}
