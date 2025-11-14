package com.myapp.demo.service;

import com.myapp.demo.dto.CreateStaffDto;
import com.myapp.demo.dto.StaffDto;
import com.myapp.demo.entity.Staff;
import com.myapp.demo.entity.Status;
import com.myapp.demo.exception.InvalidRequestException;
import com.myapp.demo.exception.ItemNotFoundException;
import com.myapp.demo.repository.StaffRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for Staff entity
 * Handles business logic, validation, and persistence
 */
@Service
@Transactional
public class StaffService {

    private static final Logger logger = LoggerFactory.getLogger(StaffService.class);

    @Autowired
    private StaffRepository staffRepository;

    /**
     * Create a new staff member
     */
    public StaffDto createStaff(CreateStaffDto dto) {
        logger.info("Creating new staff member: {}", dto.getEmail());

        validateStaffDto(dto);

        // Check if email already exists
        if (staffRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new InvalidRequestException("Email already exists: " + dto.getEmail());
        }

        Staff staff = Staff.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .designation(dto.getDesignation())
                .department(dto.getDepartment())
                .rights(dto.getRights())
                .phoneNumber(dto.getPhoneNumber())
                .status(dto.getStatus() != null ? dto.getStatus() : Status.ACTIVE)
                .build();

        staff = staffRepository.save(staff);
        logger.info("Staff created successfully with ID: {}", staff.getId());

        return convertToDto(staff);
    }

    /**
     * Get all staff members
     */
    public List<StaffDto> getAllStaff() {
        logger.info("Fetching all staff members");
        return staffRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Get a single staff member by ID
     */
    public StaffDto getStaffById(Long staffId) {
        logger.info("Fetching staff with ID: {}", staffId);
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new ItemNotFoundException("Staff not found with ID: " + staffId));
        return convertToDto(staff);
    }

    /**
     * Update a staff member
     */
    public StaffDto updateStaff(Long staffId, CreateStaffDto dto) {
        logger.info("Updating staff with ID: {}", staffId);

        validateStaffDto(dto);

        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new ItemNotFoundException("Staff not found with ID: " + staffId));

        // Check if new email already exists (and it's different from current email)
        if (!staff.getEmail().equals(dto.getEmail()) && 
            staffRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new InvalidRequestException("Email already exists: " + dto.getEmail());
        }

        staff.setName(dto.getName());
        staff.setEmail(dto.getEmail());
        staff.setDesignation(dto.getDesignation());
        staff.setDepartment(dto.getDepartment());
        staff.setRights(dto.getRights());
        staff.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getStatus() != null) {
            staff.setStatus(dto.getStatus());
        }

        staff = staffRepository.save(staff);
        logger.info("Staff updated successfully with ID: {}", staff.getId());

        return convertToDto(staff);
    }

    /**
     * Delete a staff member
     */
    public void deleteStaff(Long staffId) {
        logger.info("Deleting staff with ID: {}", staffId);

        if (!staffRepository.existsById(staffId)) {
            throw new ItemNotFoundException("Staff not found with ID: " + staffId);
        }

        staffRepository.deleteById(staffId);
        logger.info("Staff deleted successfully with ID: {}", staffId);
    }

    /**
     * Validate staff DTO
     */
    private void validateStaffDto(CreateStaffDto dto) {
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new InvalidRequestException("Name is required");
        }
        if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
            throw new InvalidRequestException("Email is required");
        }
        if (dto.getRights() == null) {
            throw new InvalidRequestException("Rights are required");
        }
    }

    /**
     * Convert entity to DTO
     */
    private StaffDto convertToDto(Staff staff) {
        return StaffDto.builder()
                .id(staff.getId())
                .name(staff.getName())
                .email(staff.getEmail())
                .designation(staff.getDesignation())
                .department(staff.getDepartment())
                .rights(staff.getRights())
                .createdDate(staff.getCreatedDate())
                .updatedDate(staff.getUpdatedDate())
                .phoneNumber(staff.getPhoneNumber())
                .status(staff.getStatus())
                .build();
    }
}
