package com.myapp.demo.config;

import com.myapp.demo.entity.Staff;
import com.myapp.demo.entity.Status;
import com.myapp.demo.entity.UserRights;
import com.myapp.demo.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Check if ANY admin exists
        if (staffRepository.countByRights(UserRights.ADMIN) == 0) {

            Staff admin = Staff.builder()
                    .name("Admin")
                    .email("admin@gmail.com")
                    .password(passwordEncoder.encode("admin123"))
                    .phoneNumber("0000000000")
                    .department("Admin")
                    .designation("Administrator")
                    .rights(UserRights.ADMIN)
                    .status(Status.ACTIVE)
                    .build();

            staffRepository.save(admin);
            System.out.println("✅ System was admin-less. Created Default Admin: admin@company.com");
        }
    }
}
