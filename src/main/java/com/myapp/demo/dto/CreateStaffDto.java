package com.myapp.demo.dto;

import com.myapp.demo.entity.Status;
import com.myapp.demo.entity.UserRights;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating Staff
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateStaffDto {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    private String designation;

    private String department;

    @NotNull(message = "Rights are required")
    private UserRights rights;

    private String phoneNumber;

    private Status status;
}
