package com.myapp.demo.service;

import com.myapp.demo.dto.DashboardStatsDTO;
import com.myapp.demo.dto.InventoryStockDto;
import com.myapp.demo.entity.InventoryStock;
import com.myapp.demo.repository.InventoryStockRepository;
import com.myapp.demo.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService {

        @Autowired
        private InventoryStockRepository inventoryStockRepository;

        @Autowired
        private StaffRepository staffRepository;

        @Autowired
        private InventoryStockService inventoryStockService;

        public DashboardStatsDTO getStats() {
                long totalProducts = inventoryStockRepository.count();
                // Assuming low stock is quantity < 2 as per user requirement
                long lowStockProducts = inventoryStockRepository.findAll().stream()
                                .filter(p -> p.getQuantity() < 2)
                                .count();
                long totalStaff = staffRepository.count();

                // Get 5 most recent products
                List<InventoryStock> recentStock = inventoryStockRepository.findAll(
                                PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdDate"))).getContent();

                // We need to convert Entity to DTO.
                // InventoryStockService has convertToDto but it is private.
                // We can duplicate logic or make it public. For now, let's just map manually or
                // use a mapper if available.
                // Or better, let's assume InventoryStockService has a method to get recent
                // products or we just map here.
                // Since I can't easily change InventoryStockService visibility without reading
                // it again and it might be messy, I'll map here.

                List<InventoryStockDto> recentProducts = recentStock.stream()
                                .map(this::convertToDto)
                                .collect(Collectors.toList());

                return DashboardStatsDTO.builder()
                                .totalProducts(totalProducts)
                                .lowStockProducts(lowStockProducts)
                                .totalStaff(totalStaff)
                                .recentProducts(recentProducts)
                                .build();
        }

        private InventoryStockDto convertToDto(InventoryStock stock) {
                return InventoryStockDto.builder()
                                .productId(stock.getProductId())
                                .productName(stock.getProductName())
                                .pricePerUnit(stock.getPricePerUnit())
                                .quantity(stock.getQuantity())
                                .status(stock.getStatus())
                                .createdDate(stock.getCreatedDate())
                                .updatedDate(stock.getUpdatedDate())
                                .build();
        }
}
