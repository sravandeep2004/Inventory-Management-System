package com.myapp.demo.service;

import com.myapp.demo.dto.CreateInventoryStockDto;
import com.myapp.demo.dto.InventoryStockDto;
import com.myapp.demo.dto.UpdateQuantityDto;
import com.myapp.demo.entity.InventoryStock;
import com.myapp.demo.entity.Status;
import com.myapp.demo.exception.InvalidRequestException;
import com.myapp.demo.exception.ItemNotFoundException;
import com.myapp.demo.repository.InventoryStockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for InventoryStock entity
 * Handles business logic, validation, and persistence
 */
@Service
@Transactional
public class InventoryStockService {

    private static final Logger logger = LoggerFactory.getLogger(InventoryStockService.class);

    @Autowired
    private InventoryStockRepository inventoryRepository;

    @Autowired
    private AlertService alertService;

    @Autowired
    private ScheduledInventoryChecker scheduledInventoryChecker;

    /**
     * Create a new inventory item
     */
    public InventoryStockDto createProduct(CreateInventoryStockDto dto) {
        logger.info("Creating new product: {}", dto.getProductName());

        validateInventoryDto(dto);

        InventoryStock item = InventoryStock.builder()
                .productName(dto.getProductName())
                .pricePerUnit(dto.getPricePerUnit())
                .quantity(dto.getQuantity())
                .status(dto.getStatus() != null ? dto.getStatus() : Status.ACTIVE)
                .build();

        item = inventoryRepository.save(item);
        logger.info("Product created successfully with ID: {}", item.getProductId());

        // Check for low stock and send alerts if needed
        alertService.checkAndNotify(item);

        return convertToDto(item);
    }

    /**
     * Get all inventory items
     */
    public List<InventoryStockDto> getAllProducts() {
        logger.info("Fetching all products");
        return inventoryRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Get a single inventory item by ID
     */
    public InventoryStockDto getProductById(Long productId) {
        logger.info("Fetching product with ID: {}", productId);
        InventoryStock item = inventoryRepository.findById(productId)
                .orElseThrow(() -> new ItemNotFoundException("Product not found with ID: " + productId));
        return convertToDto(item);
    }

    /**
     * Update an inventory item (full update)
     */
    public InventoryStockDto updateProduct(Long productId, CreateInventoryStockDto dto) {
        logger.info("Updating product with ID: {}", productId);

        validateInventoryDto(dto);

        InventoryStock item = inventoryRepository.findById(productId)
                .orElseThrow(() -> new ItemNotFoundException("Product not found with ID: " + productId));

        item.setProductName(dto.getProductName());
        item.setPricePerUnit(dto.getPricePerUnit());
        item.setQuantity(dto.getQuantity());
        if (dto.getStatus() != null) {
            item.setStatus(dto.getStatus());
        }

        item = inventoryRepository.save(item);
        logger.info("Product updated successfully with ID: {}", item.getProductId());

        // Check for low stock and send alerts if needed
        alertService.checkAndNotify(item);

        return convertToDto(item);
    }

    /**
     * Update quantity only (PATCH endpoint)
     */
    public InventoryStockDto updateQuantity(Long productId, UpdateQuantityDto dto) {
        logger.info("Updating quantity for product ID: {} to: {}", productId, dto.getQuantity());

        if (dto.getQuantity() < 0) {
            throw new InvalidRequestException("Quantity cannot be negative");
        }

        InventoryStock item = inventoryRepository.findById(productId)
                .orElseThrow(() -> new ItemNotFoundException("Product not found with ID: " + productId));

        item.setQuantity(dto.getQuantity());
        item = inventoryRepository.save(item);
        logger.info("Quantity updated successfully for product ID: {}", item.getProductId());

        // If stock is now above threshold, clear the alert for this product
        if (dto.getQuantity() >= 2) {
            logger.info("Stock replenished for product ID: {}, clearing alert", productId);
            scheduledInventoryChecker.clearProductAlert(productId);
        }

        // Check for low stock and send alerts if needed
        alertService.checkAndNotify(item);

        return convertToDto(item);
    }

    /**
     * Delete an inventory item
     */
    public void deleteProduct(Long productId) {
        logger.info("Deleting product with ID: {}", productId);

        if (!inventoryRepository.existsById(productId)) {
            throw new ItemNotFoundException("Product not found with ID: " + productId);
        }

        inventoryRepository.deleteById(productId);
        logger.info("Product deleted successfully with ID: {}", productId);
    }

    /**
     * Validate inventory DTO
     */
    private void validateInventoryDto(CreateInventoryStockDto dto) {
        if (dto.getProductName() == null || dto.getProductName().trim().isEmpty()) {
            throw new InvalidRequestException("Product name is required");
        }
        if (dto.getPricePerUnit() == null || dto.getPricePerUnit().signum() <= 0) {
            throw new InvalidRequestException("Price per unit must be greater than 0");
        }
        if (dto.getQuantity() == null || dto.getQuantity() < 0) {
            throw new InvalidRequestException("Quantity cannot be negative");
        }
    }

    /**
     * Convert entity to DTO
     */
    private InventoryStockDto convertToDto(InventoryStock item) {
        return InventoryStockDto.builder()
                .productId(item.getProductId())
                .productName(item.getProductName())
                .pricePerUnit(item.getPricePerUnit())
                .quantity(item.getQuantity())
                .totalPrice(item.getTotalPrice())
                .createdDate(item.getCreatedDate())
                .updatedDate(item.getUpdatedDate())
                .status(item.getStatus())
                .build();
    }
}
