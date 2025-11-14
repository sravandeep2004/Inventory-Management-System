package com.myapp.demo.repository;

import com.myapp.demo.entity.Staff;
import com.myapp.demo.entity.Status;
import com.myapp.demo.entity.UserRights;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Repository tests for Staff entity
 */
@DataJpaTest
class StaffRepositoryTest {

    @Autowired
    private StaffRepository staffRepository;

    private Staff testStaff;

    @BeforeEach
    void setUp() {
        testStaff = Staff.builder()
                .name("John Doe")
                .email("john@example.com")
                .designation("Manager")
                .department("Sales")
                .rights(UserRights.ADMIN)
                .phoneNumber("1234567890")
                .status(Status.ACTIVE)
                .build();
    }

    @Test
    void testSaveStaff() {
        // When
        Staff saved = staffRepository.save(testStaff);

        // Then
        assertNotNull(saved.getId());
        assertEquals("John Doe", saved.getName());
        assertEquals("john@example.com", saved.getEmail());
    }

    @Test
    void testFindByEmail() {
        // Given
        staffRepository.save(testStaff);

        // When
        Optional<Staff> found = staffRepository.findByEmail("john@example.com");

        // Then
        assertTrue(found.isPresent());
        assertEquals("John Doe", found.get().getName());
    }

    @Test
    void testFindByEmailNotFound() {
        // When
        Optional<Staff> found = staffRepository.findByEmail("nonexistent@example.com");

        // Then
        assertTrue(found.isEmpty());
    }
}
