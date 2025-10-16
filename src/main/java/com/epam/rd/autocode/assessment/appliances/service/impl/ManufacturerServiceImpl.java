package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import com.epam.rd.autocode.assessment.appliances.dto.ManufacturerDTO;
import com.epam.rd.autocode.assessment.appliances.mapper.ManufacturerMapper;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.repository.ApplianceRepository;
import com.epam.rd.autocode.assessment.appliances.repository.ManufacturerRepository;
import com.epam.rd.autocode.assessment.appliances.service.ManufacturerService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.NoSuchElementException;

@Service
public class ManufacturerServiceImpl implements ManufacturerService {
    private final ManufacturerRepository manufacturerRepository;
    private final ApplianceRepository applianceRepository;
    private final ManufacturerMapper manufacturerMapper;

    @Autowired
    public ManufacturerServiceImpl(
            ManufacturerRepository manufacturerRepository,
            ApplianceRepository applianceRepository,
            ManufacturerMapper manufacturerMapper) {
        this.manufacturerRepository = manufacturerRepository;
        this.applianceRepository = applianceRepository;
        this.manufacturerMapper = manufacturerMapper;
    }
    @Override
    @Loggable
    public Page<ManufacturerDTO> getManufacturers(int page, int size, String sort, String order) {
        Sort.Direction direction = "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sort));
        return manufacturerRepository.findAll(pageRequest)
                .map(manufacturerMapper::toDTO);
    }
    @Override
    @Loggable
    public ManufacturerDTO getManufacturerById(Long id) {
        Manufacturer manufacturer = manufacturerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Manufacturer not found"));
        return manufacturerMapper.toDTO(manufacturer);
    }
    @Override
    @Loggable
    public void createManufacturer(ManufacturerDTO manufacturerDTO) {
        Manufacturer manufacturer = manufacturerMapper.toEntity(manufacturerDTO);
        Manufacturer savedManufacturer = manufacturerRepository.save(manufacturer);
        manufacturerMapper.toDTO(savedManufacturer);
    }
    @Override
    @Loggable
    public void updateManufacturer(ManufacturerDTO manufacturerDTO) {
        Manufacturer manufacturer = manufacturerMapper.toEntity(manufacturerDTO);
        Manufacturer updatedManufacturer = manufacturerRepository.save(manufacturer);
        manufacturerMapper.toDTO(updatedManufacturer);
    }
    @Override
    @Loggable
    @Transactional
    public void deleteManufacturer(Long id) {
        applianceRepository.deleteAllByManufacturerId(id);
        manufacturerRepository.deleteById(id);
    }
}