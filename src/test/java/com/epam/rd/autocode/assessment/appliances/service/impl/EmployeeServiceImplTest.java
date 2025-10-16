package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.dto.EmployeeDTO;
import com.epam.rd.autocode.assessment.appliances.mapper.EmployeeMapper;
import com.epam.rd.autocode.assessment.appliances.model.Employee;
import com.epam.rd.autocode.assessment.appliances.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;
    private EmployeeDTO employeeDTO;

    @BeforeEach
    void setUp() {
        employee = new Employee(1L, "Jane Smith", "jane@example.com", "password123", "IT");
        employeeDTO = new EmployeeDTO(1L, "Jane Smith", "jane@example.com", "IT", "password123");
    }

    @Test
    void findById_WhenExists_ShouldReturnEmployeeDTO() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeMapper.toDTO(employee)).thenReturn(employeeDTO);

        Optional<EmployeeDTO> result = employeeService.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Jane Smith");
        verify(employeeRepository).findById(1L);
        verify(employeeMapper).toDTO(employee);
    }

    @Test
    void findById_WhenNotExists_ShouldReturnEmpty() {
        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<EmployeeDTO> result = employeeService.findById(999L);

        assertThat(result).isEmpty();
        verify(employeeRepository).findById(999L);
        verify(employeeMapper, never()).toDTO(any());
    }

    @Test
    void getAllEmployees_ShouldReturnListOfEmployees() {
        List<Employee> employees = Collections.singletonList(employee);
        when(employeeRepository.findAll()).thenReturn(employees);
        when(employeeMapper.toDTO(employee)).thenReturn(employeeDTO);

        List<EmployeeDTO> result = employeeService.getAllEmployees();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Jane Smith");
        verify(employeeRepository).findAll();
    }

    @Test
    void saveEmployee_ShouldSaveEmployee() {
        when(employeeMapper.toEntity(employeeDTO)).thenReturn(employee);
        when(employeeRepository.save(employee)).thenReturn(employee);

        employeeService.saveEmployee(employeeDTO);

        verify(employeeMapper).toEntity(employeeDTO);
        verify(employeeRepository).save(employee);
    }

    @Test
    void deleteEmployeeById_ShouldDeleteEmployee() {
        doNothing().when(employeeRepository).deleteById(1L);

        employeeService.deleteEmployeeById(1L);

        verify(employeeRepository).deleteById(1L);
    }

    @Test
    void getEmployees_ShouldReturnPageOfEmployees() {
        List<Employee> employees = Collections.singletonList(employee);
        Page<Employee> employeePage = new PageImpl<>(employees);

        when(employeeRepository.findAll(any(PageRequest.class))).thenReturn(employeePage);
        when(employeeMapper.toDTO(employee)).thenReturn(employeeDTO);

        Page<EmployeeDTO> result = employeeService.getEmployees(0, 10, "name", "asc");

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Jane Smith");
        verify(employeeRepository).findAll(PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name")));
    }

    @Test
    void getEmployees_WithDescOrder_ShouldReturnPageWithDescOrder() {
        List<Employee> employees = Collections.singletonList(employee);
        Page<Employee> employeePage = new PageImpl<>(employees);

        when(employeeRepository.findAll(any(PageRequest.class))).thenReturn(employeePage);
        when(employeeMapper.toDTO(employee)).thenReturn(employeeDTO);

        Page<EmployeeDTO> result = employeeService.getEmployees(0, 10, "department", "desc");

        verify(employeeRepository).findAll(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "department")));
    }

    @Test
    void getEmployeeDTOById_WhenExists_ShouldReturnEmployeeDTO() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeMapper.toDTO(employee)).thenReturn(employeeDTO);

        Optional<EmployeeDTO> result = employeeService.getEmployeeDTOById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Jane Smith");
    }

    @Test
    void getEmployeeDTOById_WhenNotExists_ShouldReturnEmpty() {
        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<EmployeeDTO> result = employeeService.getEmployeeDTOById(999L);

        assertThat(result).isEmpty();
    }

    @Test
    void toDTO_ShouldConvertEmployeeToDTO() {
        when(employeeMapper.toDTO(employee)).thenReturn(employeeDTO);

        EmployeeDTO result = employeeService.toDTO(employee);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Jane Smith");
        verify(employeeMapper).toDTO(employee);
    }

    @Test
    void updateEmployee_WhenExists_ShouldUpdateEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(employee)).thenReturn(employee);

        employeeService.updateEmployee(employeeDTO);

        verify(employeeRepository).findById(1L);
        verify(employeeRepository).save(employee);
        assertThat(employee.getName()).isEqualTo("Jane Smith");
        assertThat(employee.getEmail()).isEqualTo("jane@example.com");
        assertThat(employee.getDepartment()).isEqualTo("IT");
    }

    @Test
    void updateEmployee_WhenNotExists_ShouldThrowException() {
        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());
        EmployeeDTO nonExistentDTO = new EmployeeDTO(999L, "Test", "test@test.com", "IT", "pass");

        assertThatThrownBy(() -> employeeService.updateEmployee(nonExistentDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Employee not found");
    }
}