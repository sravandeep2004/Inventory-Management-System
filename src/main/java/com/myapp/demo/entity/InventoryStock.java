package com.myapp.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * InventoryStock entity representing products in inventory
 * Automatically calculates and maintains totalPrice = pricePerUnit * quantity
 */
@Entity
@Table(name = "inventory_stock")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerUnit;

    @Column(nullable = false)
    private Integer quantity;

    @Column(precision = 15, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    /**
     * Automatically set createdDate and calculate totalPrice when entity is persisted
     */
    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
        updatedDate = LocalDateTime.now();
        if (status == null) {
            status = Status.ACTIVE;
        }
        calculateTotalPrice();
    }

    /**
     * Automatically update updatedDate and recalculate totalPrice when entity is updated
     */
    @PreUpdate
    protected void onUpdate() {
        updatedDate = LocalDateTime.now();
        calculateTotalPrice();
    }

    /**
     * Calculate totalPrice as pricePerUnit * quantity
     * This method ensures consistency before every persist/update
     */
    private void calculateTotalPrice() {
        if (this.pricePerUnit != null && this.quantity != null) {
            this.totalPrice = this.pricePerUnit.multiply(new BigDecimal(this.quantity));
        }
    }
}
