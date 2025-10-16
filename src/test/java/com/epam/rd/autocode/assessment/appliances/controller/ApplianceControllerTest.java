package com.epam.rd.autocode.assessment.appliances.controller;

import com.epam.rd.autocode.assessment.appliances.dto.ApplianceDTO;
import com.epam.rd.autocode.assessment.appliances.dto.ManufacturerDTO;
import com.epam.rd.autocode.assessment.appliances.mapper.ApplianceMapper;
import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Category;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.model.PowerType;
import com.epam.rd.autocode.assessment.appliances.repository.ApplianceRepository;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ApplianceControllerTest {

    @Mock
    private ApplianceService service;
    @Mock
    private ApplianceMapper applianceMapper;

    @Mock
    private ApplianceRepository repository;

    @InjectMocks
    private ApplianceController applianceController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(applianceController).build();
    }

    @Test
    void testGetApplianceMain() throws Exception {
        Manufacturer manufacturer = new Manufacturer(1L, "Samsung");

        List<ApplianceDTO> appliances = Arrays.asList(
                new ApplianceDTO(1L, "Appliance 1", Category.BIG, "Model 1", manufacturer, PowerType.AC220, "Characteristic 1", "Description 1", 100, BigDecimal.valueOf(499.99)),
                new ApplianceDTO(2L, "Appliance 2", Category.SMALL, "Model 2", manufacturer, PowerType.ACCUMULATOR, "Characteristic 2", "Description 2", 200, BigDecimal.valueOf(999.99))
        );

        when(service.getSortedAppliances("id", "asc")).thenReturn(appliances);

        mockMvc.perform(get("/appliances")
                        .param("page", "0")
                        .param("size", "5")
                        .param("sort", "id")
                        .param("order", "asc"))
                .andExpect(status().isOk())
                .andExpect(view().name("appliance/appliances"))
                .andExpect(model().attributeExists("appliances"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attributeExists("pageSize"))
                .andExpect(model().attributeExists("sort"))
                .andExpect(model().attributeExists("order"))
                .andExpect(model().attribute("appliances", appliances.subList(0, 2)))
                .andExpect(model().attribute("currentPage", 0))
                .andExpect(model().attribute("totalPages", 1))
                .andExpect(model().attribute("pageSize", 5))
                .andExpect(model().attribute("sort", "id"))
                .andExpect(model().attribute("order", "asc"));

        verify(service).getSortedAppliances("id", "asc");
    }

    @Test
    void testShowAddApplianceForm() throws Exception {
        List<Category> categories = List.of(Category.BIG, Category.SMALL);
        List<PowerType> powerTypes = List.of(PowerType.AC220, PowerType.ACCUMULATOR);
        List<ManufacturerDTO> manufacturers = List.of(
                new ManufacturerDTO(1L, "Samsung"),
                new ManufacturerDTO(2L, "LG")
        );

        when(service.getAllCategories()).thenReturn(categories);
        when(service.getAllPowerTypes()).thenReturn(powerTypes);
        when(service.getAllManufacturers()).thenReturn(manufacturers);

        mockMvc.perform(get("/appliances/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("appliance/newAppliance"))
                .andExpect(model().attributeExists("appliance"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attributeExists("powerTypes"))
                .andExpect(model().attributeExists("manufacturers"))
                .andExpect(model().attribute("categories", categories))
                .andExpect(model().attribute("powerTypes", powerTypes))
                .andExpect(model().attribute("manufacturers", manufacturers));

        verify(service).getAllCategories();
        verify(service).getAllPowerTypes();
        verify(service).getAllManufacturers();
    }

    @Test
    void testDeleteAppliance() throws Exception {
        mockMvc.perform(get("/appliances/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/appliances"));

        verify(repository).deleteById(1L);
    }

    @Test
    void testShowEditForm() throws Exception {
        Manufacturer manufacturer = new Manufacturer(1L, "Samsung");
        Appliance appliance = new Appliance(1L, "Appliance 1", Category.BIG, "Model 1", manufacturer,
                PowerType.AC220, "Characteristic 1", "Description 1", 100, BigDecimal.valueOf(499.99));
        ApplianceDTO applianceDTO = new ApplianceDTO(1L, "Appliance 1", Category.BIG, "Model 1", manufacturer,
                PowerType.AC220, "Characteristic 1", "Description 1", 100, BigDecimal.valueOf(499.99));

        when(repository.findById(1L)).thenReturn(Optional.of(appliance));
        when(applianceMapper.toDTO(appliance)).thenReturn(applianceDTO);
        when(service.getAllCategories()).thenReturn(List.of(Category.BIG, Category.SMALL));
        when(service.getAllPowerTypes()).thenReturn(List.of(PowerType.AC220, PowerType.ACCUMULATOR));
        when(service.getAllManufacturers()).thenReturn(List.of(new ManufacturerDTO(1L, "Samsung")));

        mockMvc.perform(get("/appliances/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("appliance/editAppliance"))
                .andExpect(model().attributeExists("appliance"))
                .andExpect(model().attribute("appliance", applianceDTO))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attributeExists("powerTypes"))
                .andExpect(model().attributeExists("manufacturers"));

        verify(repository).findById(1L);
        verify(applianceMapper).toDTO(appliance);
        verify(service).getAllCategories();
        verify(service).getAllPowerTypes();
        verify(service).getAllManufacturers();
    }

    @Test
    void testUpdateAppliance_ValidData() throws Exception {
        Appliance appliance = new Appliance(1L, "Old Appliance", Category.BIG, "Old Model",
                new Manufacturer(1L, "Samsung"), PowerType.AC220, "Old Characteristic",
                "Old Description", 100, BigDecimal.valueOf(499.99));
        ApplianceDTO applianceDTO = new ApplianceDTO(1L, "Updated Appliance", Category.SMALL,
                "Updated Model", new Manufacturer(1L, "Samsung"), PowerType.ACCUMULATOR,
                "Updated Characteristic", "Updated Description", 150, BigDecimal.valueOf(599.99));

        when(repository.findById(1L)).thenReturn(Optional.of(appliance));
        doNothing().when(applianceMapper).updateEntityFromDTO(applianceDTO, appliance);

        mockMvc.perform(post("/appliances/1/update")
                        .flashAttr("appliance", applianceDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/appliances"));

        verify(repository).findById(1L);
        verify(applianceMapper).updateEntityFromDTO(applianceDTO, appliance);
        verify(repository).save(appliance);
    }

    @Test
    void testUpdateAppliance_InvalidData() throws Exception {
        ApplianceDTO applianceDTO = new ApplianceDTO(1L, "", Category.SMALL, "Updated Model", new Manufacturer(1L, "Samsung"), PowerType.ACCUMULATOR, "Updated Characteristic", "Updated Description", -1, BigDecimal.valueOf(-10.99));

        mockMvc.perform(post("/appliances/1/update")
                        .flashAttr("appliance", applianceDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("appliance/editAppliance"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attributeExists("powerTypes"))
                .andExpect(model().attributeExists("manufacturers"));

        verify(repository, never()).save(any());
    }
}
