package com.epam.rd.autocode.assessment.appliances.repository;

import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ApplianceRepository extends JpaRepository<Appliance,Long> {
    void deleteAllByManufacturerId(Long manufacturerId);
    Optional<Appliance> findByName(String name);
}
