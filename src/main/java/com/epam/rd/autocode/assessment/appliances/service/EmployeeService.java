package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.dto.EmployeeDTO;
import com.epam.rd.autocode.assessment.appliances.model.Employee;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    Optional<EmployeeDTO> findById(Long id);
    List<EmployeeDTO> getAllEmployees();
    void saveEmployee(EmployeeDTO employeeDTO);
    void deleteEmployeeById(Long id);
    Page<EmployeeDTO> getEmployees(int page, int size, String sort, String order);
    Optional<EmployeeDTO> getEmployeeDTOById(Long id);
    EmployeeDTO toDTO(Employee emp);
    void updateEmployee(EmployeeDTO dto);
}