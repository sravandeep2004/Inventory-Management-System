package com.myapp.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.demo.dto.CreateInventoryStockDto;
import com.myapp.demo.dto.UpdateQuantityDto;
import com.myapp.demo.entity.Status;
import com.myapp.demo.service.InventoryStockService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for InventoryStockController
 * Tests REST endpoints using MockMvc
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class InventoryStockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InventoryStockService inventoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateProductEndpoint() throws Exception {
        // Given
        CreateInventoryStockDto dto = CreateInventoryStockDto.builder()
                .productName("New Product")
                .pricePerUnit(new BigDecimal("49.99"))
                .quantity(20)
                .status(Status.ACTIVE)
                .build();

        // When & Then
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productId").isNumber())
                .andExpect(jsonPath("$.productName").value("New Product"))
                .andExpect(jsonPath("$.pricePerUnit").value(49.99))
                .andExpect(jsonPath("$.quantity").value(20));
    }

    @Test
    void testGetAllProductsEndpoint() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetProductByIdEndpoint() throws Exception {
        // Given
        CreateInventoryStockDto dto = CreateInventoryStockDto.builder()
                .productName("Test Product")
                .pricePerUnit(new BigDecimal("25.00"))
                .quantity(5)
                .status(Status.ACTIVE)
                .build();

        var created = inventoryService.createProduct(dto);

        // When & Then
        mockMvc.perform(get("/api/products/" + created.getProductId())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(created.getProductId()))
                .andExpect(jsonPath("$.productName").value("Test Product"));
    }

    @Test
    void testUpdateProductEndpoint() throws Exception {
        // Given
        CreateInventoryStockDto dto = CreateInventoryStockDto.builder()
                .productName("Original Product")
                .pricePerUnit(new BigDecimal("10.00"))
                .quantity(5)
                .status(Status.ACTIVE)
                .build();

        var created = inventoryService.createProduct(dto);

        CreateInventoryStockDto updateDto = CreateInventoryStockDto.builder()
                .productName("Updated Product")
                .pricePerUnit(new BigDecimal("35.00"))
                .quantity(15)
                .status(Status.ACTIVE)
                .build();

        // When & Then
        mockMvc.perform(put("/api/products/" + created.getProductId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Updated Product"))
                .andExpect(jsonPath("$.pricePerUnit").value(35.00))
                .andExpect(jsonPath("$.quantity").value(15));
    }

    @Test
    void testUpdateQuantityEndpoint() throws Exception {
        // Given
        CreateInventoryStockDto dto = CreateInventoryStockDto.builder()
                .productName("Test Product")
                .pricePerUnit(new BigDecimal("20.00"))
                .quantity(10)
                .status(Status.ACTIVE)
                .build();

        var created = inventoryService.createProduct(dto);

        UpdateQuantityDto updateDto = UpdateQuantityDto.builder()
                .quantity(30)
                .build();

        // When & Then
        mockMvc.perform(patch("/api/products/" + created.getProductId() + "/quantity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(30));
    }

    @Test
    void testDeleteProductEndpoint() throws Exception {
        // Given
        CreateInventoryStockDto dto = CreateInventoryStockDto.builder()
                .productName("Test Product")
                .pricePerUnit(new BigDecimal("10.00"))
                .quantity(5)
                .status(Status.ACTIVE)
                .build();

        var created = inventoryService.createProduct(dto);

        // When & Then
        mockMvc.perform(delete("/api/products/" + created.getProductId())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetProductNotFound() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/products/99999")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void testCreateProductValidationError() throws Exception {
        // Given - Invalid price (0 or negative)
        CreateInventoryStockDto dto = CreateInventoryStockDto.builder()
                .productName("Test Product")
                .pricePerUnit(new BigDecimal("0.00"))  // Invalid
                .quantity(5)
                .build();

        // When & Then
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"));
    }
}
