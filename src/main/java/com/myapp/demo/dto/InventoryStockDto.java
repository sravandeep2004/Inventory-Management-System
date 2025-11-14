package com.myapp.demo.dto;

import com.myapp.demo.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for InventoryStock response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryStockDto {

    private Long productId;
    private String productName;
    private BigDecimal pricePerUnit;
    private Integer quantity;
    private BigDecimal totalPrice;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Status status;
}
