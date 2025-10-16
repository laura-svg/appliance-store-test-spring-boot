package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final JdbcTemplate jdbcTemplate;
    private final BCryptPasswordEncoder passwordEncoder;
    @Override
    @Loggable
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String queryUsers = "SELECT email, password, role FROM users WHERE email = ?";
        UserDetails user;
        try {
            user = jdbcTemplate.queryForObject(queryUsers, new Object[]{email}, (rs, rowNum) -> {
                String username = rs.getString("email");
                String password = rs.getString("password");
                String role = rs.getString("role");
                if (!password.startsWith("$2a$")) {
                    password = passwordEncoder.encode(password);
                }
                return User.withUsername(username)
                        .password(password)
                        .roles(role)
                        .build();
            });
        } catch (EmptyResultDataAccessException e) {
            user = findUserInOtherTables(email);
        }
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return user;
 }
    @Loggable
    private UserDetails findUserInOtherTables(String email) {
        String queryClient = "SELECT email, password, 'CLIENT' AS role FROM client WHERE email = ?";
        try {
            return jdbcTemplate.queryForObject(queryClient, new Object[]{email}, (rs, rowNum) -> {
                String username = rs.getString("email");
                String password = rs.getString("password");
                if (!password.startsWith("$2a$")) {
                    password = passwordEncoder.encode(password);
                }
                return User.withUsername(username)
                        .password(password)
                        .roles("CLIENT")
                        .build();
            });
        } catch (EmptyResultDataAccessException ex) {
            String queryEmployee = "SELECT email, password, 'EMPLOYEE' AS role FROM employee WHERE email = ?";
            try {
                return jdbcTemplate.queryForObject(queryEmployee, new Object[]{email}, (rs, rowNum) -> {
                    String username = rs.getString("email");
                    String password = rs.getString("password");
                    if (!password.startsWith("$2a$")) {
                        password = passwordEncoder.encode(password);
                    }
                    return User.withUsername(username)
                            .password(password)
                            .roles("EMPLOYEE")
                            .build();
                });
            } catch (EmptyResultDataAccessException ex2) {
                return null;
            }
        }
    }
}