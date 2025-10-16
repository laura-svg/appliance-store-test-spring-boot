package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.dto.ApplianceDTO;
import com.epam.rd.autocode.assessment.appliances.dto.ManufacturerDTO;
import com.epam.rd.autocode.assessment.appliances.mapper.ApplianceMapper;
import com.epam.rd.autocode.assessment.appliances.mapper.ManufacturerMapper;
import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Category;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.model.PowerType;
import com.epam.rd.autocode.assessment.appliances.repository.ApplianceRepository;
import com.epam.rd.autocode.assessment.appliances.repository.ManufacturerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplianceServiceImplTest {

    @Mock
    private ApplianceMapper applianceMapper;

    @Mock
    private ManufacturerMapper manufacturerMapper;

    @Mock
    private ApplianceRepository applianceRepository;

    @Mock
    private ManufacturerRepository manufacturerRepository;

    @InjectMocks
    private ApplianceServiceImpl applianceService;

    private Appliance appliance;
    private ApplianceDTO applianceDTO;
    private Manufacturer manufacturer;
    private ManufacturerDTO manufacturerDTO;

    @BeforeEach
    void setUp() {
        manufacturer = new Manufacturer(1L, "Samsung");
        manufacturerDTO = new ManufacturerDTO(1L, "Samsung");

        appliance = new Appliance();
        appliance.setId(1L);
        appliance.setName("Refrigerator");
        appliance.setCategory(Category.BIG);
        appliance.setModel("RF28R7351SR");
        appliance.setManufacturer(manufacturer);
        appliance.setPowerType(PowerType.AC220);
        appliance.setCharacteristic("Energy efficient");
        appliance.setDescription("French door refrigerator");
        appliance.setPower(150);
        appliance.setPrice(new BigDecimal("1200.00"));

        applianceDTO = ApplianceDTO.builder()
                .id(1L)
                .name("Refrigerator")
                .category(Category.BIG)
                .model("RF28R7351SR")
                .manufacturer(manufacturer)
                .powerType(PowerType.AC220)
                .characteristic("Energy efficient")
                .description("French door refrigerator")
                .power(150)
                .price(new BigDecimal("1200.00"))
                .build();
    }

    @Test
    void findByName_WhenExists_ShouldReturnApplianceDTO() {
        when(applianceRepository.findByName("Refrigerator")).thenReturn(Optional.of(appliance));
        when(applianceMapper.toDTO(appliance)).thenReturn(applianceDTO);

        Optional<ApplianceDTO> result = applianceService.findByName("Refrigerator");

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Refrigerator");
        verify(applianceRepository).findByName("Refrigerator");
        verify(applianceMapper).toDTO(appliance);
    }

    @Test
    void findByName_WhenNotExists_ShouldReturnEmpty() {
        when(applianceRepository.findByName("NonExistent")).thenReturn(Optional.empty());

        Optional<ApplianceDTO> result = applianceService.findByName("NonExistent");

        assertThat(result).isEmpty();
        verify(applianceRepository).findByName("NonExistent");
        verify(applianceMapper, never()).toDTO(any());
    }

    @Test
    void findAll_ShouldReturnListOfAppliances() {
        List<Appliance> appliances = Collections.singletonList(appliance);
        when(applianceRepository.findAll()).thenReturn(appliances);
        when(applianceMapper.toDTO(appliance)).thenReturn(applianceDTO);

        List<ApplianceDTO> result = applianceService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Refrigerator");
        verify(applianceRepository).findAll();
    }

    @Test
    void getAllManufacturers_ShouldReturnListOfManufacturers() {
        List<Manufacturer> manufacturers = Collections.singletonList(manufacturer);
        when(manufacturerRepository.findAll()).thenReturn(manufacturers);
        when(manufacturerMapper.toDTO(manufacturer)).thenReturn(manufacturerDTO);

        List<ManufacturerDTO> result = applianceService.getAllManufacturers();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Samsung");
        verify(manufacturerRepository).findAll();
    }

    @Test
    void getAllCategories_ShouldReturnAllCategories() {
        List<Category> result = applianceService.getAllCategories();

        assertThat(result).containsExactlyInAnyOrder(Category.BIG, Category.SMALL);
    }

    @Test
    void getAllPowerTypes_ShouldReturnAllPowerTypes() {
        List<PowerType> result = applianceService.getAllPowerTypes();

        assertThat(result).containsExactlyInAnyOrder(PowerType.AC220, PowerType.AC110, PowerType.ACCUMULATOR);
    }

    @Test
    void findById_WhenExists_ShouldReturnApplianceDTO() {
        when(applianceRepository.findById(1L)).thenReturn(Optional.of(appliance));
        when(applianceMapper.toDTO(appliance)).thenReturn(applianceDTO);

        Optional<ApplianceDTO> result = applianceService.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        verify(applianceRepository).findById(1L);
    }

    @Test
    void findById_WhenNotExists_ShouldReturnEmpty() {
        when(applianceRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<ApplianceDTO> result = applianceService.findById(999L);

        assertThat(result).isEmpty();
        verify(applianceRepository).findById(999L);
    }

    @Test
    void getAppliances_WithSearchFilter_ShouldFilterByName() {
        List<Appliance> appliances = Collections.singletonList(appliance);
        when(applianceRepository.findAll()).thenReturn(appliances);
        when(applianceMapper.toDTO(appliance)).thenReturn(applianceDTO);

        List<ApplianceDTO> result = applianceService.getAppliances("refrig", null, null, null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).containsIgnoringCase("refrig");
    }

    @Test
    void getAppliances_WithCategoryFilter_ShouldFilterByCategory() {
        List<Appliance> appliances = Collections.singletonList(appliance);
        when(applianceRepository.findAll()).thenReturn(appliances);
        when(applianceMapper.toDTO(appliance)).thenReturn(applianceDTO);

        List<ApplianceDTO> result = applianceService.getAppliances(null, null, "BIG", null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCategory()).isEqualTo(Category.BIG);
    }

    @Test
    void getAppliances_WithManufacturerFilter_ShouldFilterByManufacturer() {
        List<Appliance> appliances = Collections.singletonList(appliance);
        when(applianceRepository.findAll()).thenReturn(appliances);
        when(applianceMapper.toDTO(appliance)).thenReturn(applianceDTO);

        List<ApplianceDTO> result = applianceService.getAppliances(null, null, null, "Samsung");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getManufacturer().getName()).isEqualTo("Samsung");
    }

    @Test
    void getAppliances_WithPriceSorting_ShouldSortByPrice() {
        Appliance appliance2 = new Appliance();
        appliance2.setId(2L);
        appliance2.setName("Microwave");
        appliance2.setPrice(new BigDecimal("500.00"));
        appliance2.setManufacturer(manufacturer);
        appliance2.setCategory(Category.SMALL);

        ApplianceDTO applianceDTO2 = ApplianceDTO.builder()
                .id(2L)
                .name("Microwave")
                .price(new BigDecimal("500.00"))
                .manufacturer(manufacturer)
                .category(Category.SMALL)
                .build();

        List<Appliance> appliances = Arrays.asList(appliance, appliance2);
        when(applianceRepository.findAll()).thenReturn(appliances);
        when(applianceMapper.toDTO(appliance)).thenReturn(applianceDTO);
        when(applianceMapper.toDTO(appliance2)).thenReturn(applianceDTO2);

        List<ApplianceDTO> result = applianceService.getAppliances(null, "price", null, null);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getPrice()).isLessThan(result.get(1).getPrice());
    }

    @Test
    void getAppliances_WithNameSorting_ShouldSortByName() {
        Appliance appliance2 = new Appliance();
        appliance2.setId(2L);
        appliance2.setName("Microwave");
        appliance2.setManufacturer(manufacturer);
        appliance2.setCategory(Category.SMALL);
        appliance2.setPrice(new BigDecimal("500.00"));

        ApplianceDTO applianceDTO2 = ApplianceDTO.builder()
                .id(2L)
                .name("Microwave")
                .manufacturer(manufacturer)
                .category(Category.SMALL)
                .price(new BigDecimal("500.00"))
                .build();

        List<Appliance> appliances = Arrays.asList(appliance, appliance2);
        when(applianceRepository.findAll()).thenReturn(appliances);
        when(applianceMapper.toDTO(appliance)).thenReturn(applianceDTO);
        when(applianceMapper.toDTO(appliance2)).thenReturn(applianceDTO2);

        List<ApplianceDTO> result = applianceService.getAppliances(null, "name", null, null);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Microwave");
        assertThat(result.get(1).getName()).isEqualTo("Refrigerator");
    }

    @Test
    void getApplianceForEdit_WhenExists_ShouldReturnApplianceDTO() {
        when(applianceRepository.findById(1L)).thenReturn(Optional.of(appliance));
        when(applianceMapper.toDTO(appliance)).thenReturn(applianceDTO);

        ApplianceDTO result = applianceService.getApplianceForEdit(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(applianceRepository).findById(1L);
    }

    @Test
    void getApplianceForEdit_WhenNotExists_ShouldThrowException() {
        when(applianceRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> applianceService.getApplianceForEdit(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid appliance Id:999");
    }

    @Test
    void getEditFormAttributes_ShouldReturnMapWithAttributes() {
        List<Manufacturer> manufacturers = Collections.singletonList(manufacturer);
        when(manufacturerRepository.findAll()).thenReturn(manufacturers);
        when(manufacturerMapper.toDTO(manufacturer)).thenReturn(manufacturerDTO);

        Map<String, Object> result = applianceService.getEditFormAttributes();

        assertThat(result).containsKeys("categories", "powerTypes", "manufacturers");
        assertThat((List<?>) result.get("categories")).hasSize(2);
        assertThat((List<?>) result.get("powerTypes")).hasSize(3);
        assertThat((List<?>) result.get("manufacturers")).hasSize(1);
    }

    @Test
    void getSortedAppliances_WithAscendingOrder_ShouldReturnSortedList() {
        List<Appliance> appliances = Collections.singletonList(appliance);
        when(applianceRepository.findAll(any(Sort.class))).thenReturn(appliances);
        when(applianceMapper.toDTO(appliance)).thenReturn(applianceDTO);

        List<ApplianceDTO> result = applianceService.getSortedAppliances("name", "asc");

        assertThat(result).hasSize(1);
        verify(applianceRepository).findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @Test
    void getSortedAppliances_WithDescendingOrder_ShouldReturnSortedList() {
        List<Appliance> appliances = Collections.singletonList(appliance);
        when(applianceRepository.findAll(any(Sort.class))).thenReturn(appliances);
        when(applianceMapper.toDTO(appliance)).thenReturn(applianceDTO);

        List<ApplianceDTO> result = applianceService.getSortedAppliances("price", "desc");

        assertThat(result).hasSize(1);
        verify(applianceRepository).findAll(Sort.by(Sort.Direction.DESC, "price"));
    }
}