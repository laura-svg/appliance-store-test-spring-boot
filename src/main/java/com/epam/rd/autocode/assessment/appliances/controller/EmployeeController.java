package com.epam.rd.autocode.assessment.appliances.controller;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import com.epam.rd.autocode.assessment.appliances.dto.EmployeeDTO;
import com.epam.rd.autocode.assessment.appliances.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/employees")
@AllArgsConstructor
public class EmployeeController {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
    private EmployeeService employeeService;

    @GetMapping
    @Loggable
    public String getEmployeesMain(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String order,
            Model model) {
        Page<EmployeeDTO> employees = employeeService.getEmployees(page, size, sort, order);
        int totalPages = employees.getTotalPages();
        if (totalPages == 0) {
            model.addAttribute("employees", List.of());
            model.addAttribute("currentPage", 0);
            model.addAttribute("totalPages", 0);
        } else {
            model.addAttribute("employees", employees.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
        }
        model.addAttribute("totalItems", employees.getTotalElements());
        model.addAttribute("sort", sort);
        model.addAttribute("order", order);
        return "employee/employees";
    }
    @GetMapping("/add")
    @Loggable
    public String getNewEmployee(Model model) {
        model.addAttribute("employee", new EmployeeDTO());
        return "employee/newEmployee";
    }
    @PostMapping("/add-employee")
    @Loggable
    public String postNewEmployee(@Valid @ModelAttribute("employee") EmployeeDTO employeeDTO,
                                  BindingResult result) {
        if (result.hasErrors()) {
            return "employee/newEmployee";
        }
        employeeService.saveEmployee(employeeDTO);
        return "redirect:/employees";
    }
    @GetMapping("/{id}/edit")
    @Loggable
    public String getEditEmployeeForm(@PathVariable("id") Long id, Model model) {
        Optional<EmployeeDTO> employeeDTOOptional = employeeService.getEmployeeDTOById(id);
        if (employeeDTOOptional.isPresent()) {
            model.addAttribute("employee", employeeDTOOptional.get());
            return "employee/editEmployee";
        } else {
            return "redirect:/employees";
        }
    }
    @PostMapping("/update")
    @Loggable
    public String updateEmployee(@Valid @ModelAttribute("employee") EmployeeDTO employeeDTO,
                                 BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.error("Validation errors: {}", result.getAllErrors());
            model.addAttribute("employee", employeeDTO);
            return "employee/editEmployee";
        }
        employeeService.saveEmployee(employeeDTO);
        return "redirect:/employees";
    }
    @GetMapping("/{id}/delete")
    @Loggable
    public String deleteEmployee(@PathVariable("id") Long id) {
        employeeService.deleteEmployeeById(id);
        return "redirect:/employees";
    }
}