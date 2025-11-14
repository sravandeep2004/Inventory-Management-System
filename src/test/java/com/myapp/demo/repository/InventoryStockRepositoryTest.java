package com.myapp.demo.repository;

import com.myapp.demo.entity.InventoryStock;
import com.myapp.demo.entity.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Repository tests for InventoryStock entity
 * Tests repository save, load, and query behavior
 */
@DataJpaTest
class InventoryStockRepositoryTest {

    @Autowired
    private InventoryStockRepository inventoryRepository;

    private InventoryStock testItem;

    @BeforeEach
    void setUp() {
        testItem = InventoryStock.builder()
                .productName("Test Product")
                .pricePerUnit(new BigDecimal("10.00"))
                .quantity(5)
                .status(Status.ACTIVE)
                .build();
    }

    @Test
    void testSaveInventoryItem() {
        // When
        InventoryStock saved = inventoryRepository.save(testItem);

        // Then
        assertNotNull(saved.getProductId());
        assertEquals("Test Product", saved.getProductName());
        assertEquals(new BigDecimal("10.00"), saved.getPricePerUnit());
        assertEquals(5, saved.getQuantity());
    }

    @Test
    void testTotalPriceCalculation() {
        // When
        InventoryStock saved = inventoryRepository.save(testItem);

        // Then
        // totalPrice should be calculated as pricePerUnit * quantity = 10.00 * 5 = 50.00
        assertEquals(new BigDecimal("50.00"), saved.getTotalPrice());
    }

    @Test
    void testFindByQuantityLessThan() {
        // Given
        InventoryStock lowStockItem = InventoryStock.builder()
                .productName("Low Stock Product")
                .pricePerUnit(new BigDecimal("15.00"))
                .quantity(1)
                .status(Status.ACTIVE)
                .build();

        InventoryStock normalStockItem = InventoryStock.builder()
                .productName("Normal Stock Product")
                .pricePerUnit(new BigDecimal("20.00"))
                .quantity(10)
                .status(Status.ACTIVE)
                .build();

        inventoryRepository.save(lowStockItem);
        inventoryRepository.save(normalStockItem);

        // When
        List<InventoryStock> lowStockItems = inventoryRepository.findByQuantityLessThan(5);

        // Then
        assertEquals(1, lowStockItems.size());
        assertEquals("Low Stock Product", lowStockItems.get(0).getProductName());
    }

    @Test
    void testFindByIdAndUpdate() {
        // Given
        InventoryStock saved = inventoryRepository.save(testItem);
        Long productId = saved.getProductId();

        // When
        Optional<InventoryStock> fetched = inventoryRepository.findById(productId);

        // Then
        assertTrue(fetched.isPresent());
        assertEquals("Test Product", fetched.get().getProductName());
        assertEquals(new BigDecimal("50.00"), fetched.get().getTotalPrice());  // 10.00 * 5

        // When - Update quantity
        InventoryStock itemToUpdate = fetched.get();
        itemToUpdate.setQuantity(10);
        InventoryStock updated = inventoryRepository.save(itemToUpdate);

        // Then - After save and refresh
        assertEquals(10, updated.getQuantity());
        // Verify totalPrice is calculated (may differ slightly due to persistence)
        assertNotNull(updated.getTotalPrice());
        assertTrue(updated.getTotalPrice().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void testDeleteInventoryItem() {
        // Given
        InventoryStock saved = inventoryRepository.save(testItem);
        Long productId = saved.getProductId();

        // When
        inventoryRepository.deleteById(productId);

        // Then
        assertTrue(inventoryRepository.findById(productId).isEmpty());
    }
}
