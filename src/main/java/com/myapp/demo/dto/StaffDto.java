package com.myapp.demo.dto;

import com.myapp.demo.entity.Status;
import com.myapp.demo.entity.UserRights;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for Staff response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffDto {

    private Long id;
    private String name;
    private String email;
    private String designation;
    private String department;
    private UserRights rights;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String phoneNumber;
    private Status status;
}
