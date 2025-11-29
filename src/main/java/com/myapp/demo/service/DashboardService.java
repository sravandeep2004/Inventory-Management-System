package com.myapp.demo.service;

import com.myapp.demo.dto.DashboardStatsDTO;
import com.myapp.demo.dto.InventoryStockDto;
import com.myapp.demo.entity.InventoryStock;
import com.myapp.demo.entity.Status;
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

        public DashboardStatsDTO getStats() {
                List<InventoryStock> allProducts = inventoryStockRepository.findAll();

                long totalProducts = allProducts.size();

                // Assuming low stock is quantity < 2 as per user requirement
                long lowStockProducts = allProducts.stream()
                                .filter(p -> p.getQuantity() < 2)
                                .count();

                long activeProducts = allProducts.stream()
                                .filter(p -> p.getStatus() == Status.ACTIVE)
                                .count();

                long inactiveProducts = allProducts.stream()
                                .filter(p -> p.getStatus() == Status.INACTIVE)
                                .count();

                long totalStaff = staffRepository.count();

                // Get 5 most recent products
                List<InventoryStock> recentStock = inventoryStockRepository.findAll(
                                PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdDate"))).getContent();

                List<InventoryStockDto> recentProducts = recentStock.stream()
                                .map(this::convertToDto)
                                .collect(Collectors.toList());

                return DashboardStatsDTO.builder()
                                .totalProducts(totalProducts)
                                .lowStockProducts(lowStockProducts)
                                .activeProducts(activeProducts)
                                .inactiveProducts(inactiveProducts)
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
