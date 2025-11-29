package com.myapp.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {
    private long totalProducts;
    private long lowStockProducts;
    private long activeProducts;
    private long inactiveProducts;
    private long totalStaff; // Only for admin
    private List<InventoryStockDto> recentProducts;
}