package com.myapp.demo.controller;

import com.myapp.demo.dto.CreateStaffDto;
import com.myapp.demo.dto.StaffDto;
import com.myapp.demo.service.StaffService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Staff operations
 * Provides CRUD endpoints for staff management
 */
@RestController
@RequestMapping("/api/staff")
public class StaffController {

    private static final Logger logger = LoggerFactory.getLogger(StaffController.class);

    @Autowired
    private StaffService staffService;

    /**
     * Create a new staff member
     * POST /api/staff
     */
    @PostMapping
    public ResponseEntity<StaffDto> createStaff(
            @Valid @RequestBody CreateStaffDto dto) {
        logger.info("Creating new staff member");
        StaffDto result = staffService.createStaff(dto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    /**
     * Get all staff members
     * GET /api/staff
     */
    @GetMapping
    public ResponseEntity<List<StaffDto>> getAllStaff() {
        logger.info("Fetching all staff members");
        List<StaffDto> staff = staffService.getAllStaff();
        return ResponseEntity.ok(staff);
    }

    /**
     * Get a staff member by ID
     * GET /api/staff/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<StaffDto> getStaffById(@PathVariable Long id) {
        logger.info("Fetching staff with ID: {}", id);
        StaffDto staff = staffService.getStaffById(id);
        return ResponseEntity.ok(staff);
    }

    /**
     * Update a staff member
     * PUT /api/staff/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<StaffDto> updateStaff(
            @PathVariable Long id,
            @Valid @RequestBody CreateStaffDto dto) {
        logger.info("Updating staff with ID: {}", id);
        StaffDto result = staffService.updateStaff(id, dto);
        return ResponseEntity.ok(result);
    }

    /**
     * Delete a staff member
     * DELETE /api/staff/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStaff(@PathVariable Long id) {
        logger.info("Deleting staff with ID: {}", id);
        staffService.deleteStaff(id);
        return ResponseEntity.noContent().build();
    }
}
