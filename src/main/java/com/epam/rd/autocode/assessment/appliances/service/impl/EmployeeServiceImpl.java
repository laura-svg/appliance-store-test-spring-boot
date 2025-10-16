package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import com.epam.rd.autocode.assessment.appliances.controller.EmployeeController;
import com.epam.rd.autocode.assessment.appliances.dto.EmployeeDTO;
import com.epam.rd.autocode.assessment.appliances.mapper.EmployeeMapper;
import com.epam.rd.autocode.assessment.appliances.model.Employee;
import com.epam.rd.autocode.assessment.appliances.repository.EmployeeRepository;
import com.epam.rd.autocode.assessment.appliances.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    @Override
    @Loggable
    public Optional<EmployeeDTO> findById(Long id) {
        return employeeRepository.findById(id)
                .map(employeeMapper::toDTO);
    }
    @Override
    @Loggable
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(employeeMapper::toDTO)
                .collect(Collectors.toList());
    }
    public void saveEmployee(EmployeeDTO employeeDTO) {
        logger.info("Saving employee: {}", employeeDTO.getId());
        employeeRepository.save(employeeMapper.toEntity(employeeDTO));
        logger.info("Employee saved: {}", employeeDTO.getId());
    }
    public void deleteEmployeeById(Long id) {
        employeeRepository.deleteById(id);
    }
    public Page<EmployeeDTO> getEmployees(int page, int size, String sort, String order) {
        Sort.Direction direction = order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sort));
        return employeeRepository.findAll(pageRequest)
                .map(employeeMapper::toDTO);
    }
    public Optional<EmployeeDTO> getEmployeeDTOById(Long id) {
        return employeeRepository.findById(id)
                .map(employeeMapper::toDTO);
    }
    @Override
    @Loggable
    public EmployeeDTO toDTO(Employee emp) {
        return employeeMapper.toDTO(emp);
    }
    @Override
    @Loggable
    public void updateEmployee(EmployeeDTO dto) {
        Employee emp = employeeRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
        emp.setName(dto.getName());
        emp.setEmail(dto.getEmail());
        emp.setDepartment(dto.getDepartment());
        employeeRepository.save(emp);
    }
}