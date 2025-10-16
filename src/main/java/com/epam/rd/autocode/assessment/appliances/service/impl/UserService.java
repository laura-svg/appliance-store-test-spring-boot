package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import com.epam.rd.autocode.assessment.appliances.dto.ClientDTO;
import com.epam.rd.autocode.assessment.appliances.dto.EmployeeDTO;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.model.Employee;
import com.epam.rd.autocode.assessment.appliances.repository.ClientRepository;
import com.epam.rd.autocode.assessment.appliances.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final JdbcTemplate jdbcTemplate;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    @Loggable
    public void registerClient(ClientDTO clientDTO) {
        if (clientRepository.existsByEmail(clientDTO.getUserEmail())) {
            throw new IllegalArgumentException("Client with this email already exists.");
        }
        String checkUserQuery = "SELECT COUNT(*) FROM users WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(checkUserQuery, Integer.class, clientDTO.getUserEmail());
        if (count != null && count > 0) {
            throw new IllegalArgumentException("User with this email already exists.");
        }
        Client client = new Client();
        client.setName(clientDTO.getUserName());
        client.setEmail(clientDTO.getUserEmail());
        client.setPassword(passwordEncoder.encode(clientDTO.getUserPassword()));
        client.setCard(clientDTO.getClientCard());
        clientRepository.save(client);
        String insertUserQuery = "INSERT INTO users (email, password, role) VALUES (?, ?, ?)";
        jdbcTemplate.update(insertUserQuery, client.getEmail(), client.getPassword(), "CLIENT");
    }
    @Transactional
    @Loggable
    public void changeClientToEmployee(EmployeeDTO employeeDTO) {
        Client client = clientRepository.findById(employeeDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));
        Employee employee = new Employee();
        employee.setName(client.getName());
        employee.setEmail(client.getEmail());
        employee.setPassword(client.getPassword());
        employee.setDepartment(employeeDTO.getDepartment());
        employeeRepository.save(employee);
        clientRepository.delete(client);
    }
}