package com.myapp.demo.controller;

import com.myapp.demo.dto.StaffDto;
import com.myapp.demo.entity.Staff;
import com.myapp.demo.repository.StaffRepository;
import com.myapp.demo.security.CustomUserDetailsService;
import com.myapp.demo.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private StaffRepository staffRepository;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody Map<String, String> authenticationRequest)
            throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.get("email"),
                            authenticationRequest.get("password")));
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.get("email"));
        final String jwt = jwtUtil.generateToken(userDetails);

        Staff staff = staffRepository.findByEmail(authenticationRequest.get("email")).orElseThrow();

        Map<String, Object> response = new HashMap<>();
        response.put("token", jwt);
        response.put("role", staff.getRights());
        response.put("name", staff.getName());
        response.put("email", staff.getEmail());

        return ResponseEntity.ok(response);
    }

    @Autowired
    private com.myapp.demo.service.StaffService staffService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody com.myapp.demo.dto.CreateStaffDto createStaffDto) {
        try {
            com.myapp.demo.dto.StaffDto createdStaff = staffService.createStaff(createStaffDto);
            return ResponseEntity.ok(createdStaff);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
