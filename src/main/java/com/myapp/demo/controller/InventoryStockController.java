package com.myapp.demo.controller;

import com.myapp.demo.dto.CreateInventoryStockDto;
import com.myapp.demo.dto.InventoryStockDto;
import com.myapp.demo.dto.UpdateQuantityDto;
import com.myapp.demo.service.InventoryStockService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for InventoryStock operations
 * Provides CRUD endpoints for product inventory management
 */
@RestController
@RequestMapping("/api/products")
public class InventoryStockController {

    private static final Logger logger = LoggerFactory.getLogger(InventoryStockController.class);

    @Autowired
    private InventoryStockService inventoryService;

    /**
     * Create a new product
     * POST /api/products
     */
    @PostMapping
    public ResponseEntity<InventoryStockDto> createProduct(
            @Valid @RequestBody CreateInventoryStockDto dto) {
        logger.info("Creating new product");
        InventoryStockDto result = inventoryService.createProduct(dto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    /**
     * Get all products
     * GET /api/products
     */
    @GetMapping
    public ResponseEntity<List<InventoryStockDto>> getAllProducts() {
        logger.info("Fetching all products");
        List<InventoryStockDto> products = inventoryService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * Get a product by ID
     * GET /api/products/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<InventoryStockDto> getProductById(@PathVariable Long id) {
        logger.info("Fetching product with ID: {}", id);
        InventoryStockDto product = inventoryService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    /**
     * Update a product (full update)
     * PUT /api/products/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<InventoryStockDto> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody CreateInventoryStockDto dto) {
        logger.info("Updating product with ID: {}", id);
        InventoryStockDto result = inventoryService.updateProduct(id, dto);
        return ResponseEntity.ok(result);
    }

    /**
     * Update product quantity only (partial update)
     * PATCH /api/products/{id}/quantity
     */
    @PatchMapping("/{id}/quantity")
    public ResponseEntity<InventoryStockDto> updateQuantity(
            @PathVariable Long id,
            @Valid @RequestBody UpdateQuantityDto dto) {
        logger.info("Updating quantity for product ID: {}", id);
        InventoryStockDto result = inventoryService.updateQuantity(id, dto);
        return ResponseEntity.ok(result);
    }

    /**
     * Delete a product
     * DELETE /api/products/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        logger.info("Deleting product with ID: {}", id);
        inventoryService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
