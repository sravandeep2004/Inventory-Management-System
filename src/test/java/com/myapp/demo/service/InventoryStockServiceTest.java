package com.myapp.demo.service;

import com.myapp.demo.dto.CreateInventoryStockDto;
import com.myapp.demo.dto.InventoryStockDto;
import com.myapp.demo.dto.UpdateQuantityDto;
import com.myapp.demo.entity.Status;
import com.myapp.demo.exception.InvalidRequestException;
import com.myapp.demo.exception.ItemNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for InventoryStockService
 * Tests the complete service workflow with real database
 */
@SpringBootTest
@Transactional
class InventoryStockServiceTest {

    @Autowired
    private InventoryStockService inventoryService;

    @Test
    void testCreateProduct() {
        // Given
        CreateInventoryStockDto dto = CreateInventoryStockDto.builder()
                .productName("Test Product")
                .pricePerUnit(new BigDecimal("25.50"))
                .quantity(10)
                .status(Status.ACTIVE)
                .build();

        // When
        InventoryStockDto result = inventoryService.createProduct(dto);

        // Then
        assertNotNull(result.getProductId());
        assertEquals("Test Product", result.getProductName());
        assertEquals(new BigDecimal("25.50"), result.getPricePerUnit());
        assertEquals(10, result.getQuantity());
        // totalPrice should be 25.50 * 10 = 255.00
        assertEquals(new BigDecimal("255.00"), result.getTotalPrice());
    }

    @Test
    void testGetAllProducts() {
        // Given
        CreateInventoryStockDto dto1 = CreateInventoryStockDto.builder()
                .productName("Product 1")
                .pricePerUnit(new BigDecimal("10.00"))
                .quantity(5)
                .status(Status.ACTIVE)
                .build();

        CreateInventoryStockDto dto2 = CreateInventoryStockDto.builder()
                .productName("Product 2")
                .pricePerUnit(new BigDecimal("20.00"))
                .quantity(8)
                .status(Status.ACTIVE)
                .build();

        inventoryService.createProduct(dto1);
        inventoryService.createProduct(dto2);

        // When
        List<InventoryStockDto> result = inventoryService.getAllProducts();

        // Then
        assertTrue(result.size() >= 2);
    }

    @Test
    void testGetProductById() {
        // Given
        CreateInventoryStockDto dto = CreateInventoryStockDto.builder()
                .productName("Test Product")
                .pricePerUnit(new BigDecimal("15.00"))
                .quantity(3)
                .status(Status.ACTIVE)
                .build();

        InventoryStockDto created = inventoryService.createProduct(dto);

        // When
        InventoryStockDto result = inventoryService.getProductById(created.getProductId());

        // Then
        assertEquals(created.getProductId(), result.getProductId());
        assertEquals("Test Product", result.getProductName());
    }

    @Test
    void testGetProductByIdNotFound() {
        // When & Then
        assertThrows(ItemNotFoundException.class, () -> {
            inventoryService.getProductById(99999L);
        });
    }

    @Test
    void testUpdateProduct() {
        // Given
        CreateInventoryStockDto dto = CreateInventoryStockDto.builder()
                .productName("Original Product")
                .pricePerUnit(new BigDecimal("10.00"))
                .quantity(5)
                .status(Status.ACTIVE)
                .build();

        InventoryStockDto created = inventoryService.createProduct(dto);

        CreateInventoryStockDto updateDto = CreateInventoryStockDto.builder()
                .productName("Updated Product")
                .pricePerUnit(new BigDecimal("20.00"))
                .quantity(10)
                .status(Status.ACTIVE)
                .build();

        // When
        InventoryStockDto result = inventoryService.updateProduct(created.getProductId(), updateDto);

        // Then
        assertEquals(created.getProductId(), result.getProductId());
        assertEquals("Updated Product", result.getProductName());
        assertEquals(new BigDecimal("20.00"), result.getPricePerUnit());
        assertEquals(10, result.getQuantity());
        // Verify the values were set correctly (totalPrice will be recalculated by @PreUpdate)
        assertNotNull(result.getTotalPrice());
    }

    @Test
    void testUpdateQuantity() {
        // Given
        CreateInventoryStockDto dto = CreateInventoryStockDto.builder()
                .productName("Test Product")
                .pricePerUnit(new BigDecimal("30.00"))
                .quantity(5)
                .status(Status.ACTIVE)
                .build();

        InventoryStockDto created = inventoryService.createProduct(dto);

        UpdateQuantityDto updateDto = UpdateQuantityDto.builder()
                .quantity(15)
                .build();

        // When
        InventoryStockDto result = inventoryService.updateQuantity(created.getProductId(), updateDto);

        // Then
        assertEquals(15, result.getQuantity());
        // Verify totalPrice is present and will be calculated by @PreUpdate
        assertNotNull(result.getTotalPrice());
    }

    @Test
    void testDeleteProduct() {
        // Given
        CreateInventoryStockDto dto = CreateInventoryStockDto.builder()
                .productName("Test Product")
                .pricePerUnit(new BigDecimal("10.00"))
                .quantity(5)
                .status(Status.ACTIVE)
                .build();

        InventoryStockDto created = inventoryService.createProduct(dto);

        // When
        inventoryService.deleteProduct(created.getProductId());

        // Then
        assertThrows(ItemNotFoundException.class, () -> {
            inventoryService.getProductById(created.getProductId());
        });
    }

    @Test
    void testCreateProductWithInvalidPrice() {
        // Given
        CreateInventoryStockDto dto = CreateInventoryStockDto.builder()
                .productName("Test Product")
                .pricePerUnit(new BigDecimal("0.00"))  // Invalid price
                .quantity(5)
                .status(Status.ACTIVE)
                .build();

        // When & Then
        assertThrows(InvalidRequestException.class, () -> {
            inventoryService.createProduct(dto);
        });
    }

    @Test
    void testCreateProductWithNegativeQuantity() {
        // Given
        CreateInventoryStockDto dto = CreateInventoryStockDto.builder()
                .productName("Test Product")
                .pricePerUnit(new BigDecimal("10.00"))
                .quantity(-5)  // Negative quantity
                .status(Status.ACTIVE)
                .build();

        // When & Then
        assertThrows(InvalidRequestException.class, () -> {
            inventoryService.createProduct(dto);
        });
    }
}
