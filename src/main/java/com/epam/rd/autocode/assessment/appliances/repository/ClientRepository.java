package com.epam.rd.autocode.assessment.appliances.repository;


import com.epam.rd.autocode.assessment.appliances.model.Client;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    @Query("SELECT c FROM Client c WHERE c.email = :email")
    Optional<Client> findByEmail(@Param("email") String email);
    Page<Client> findAll(Pageable pageble);
    boolean existsByEmail(String email);
    Client findByName(String username);
}
