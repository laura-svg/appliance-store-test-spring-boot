package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.dto.ClientDTO;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Optional;

public interface ClientService {
    Page<ClientDTO> getClients(int page, int size, String sort, String order);
    Optional<ClientDTO> findById(Long id);
    List<ClientDTO> getAllClients();
    void saveClient(ClientDTO clientDTO);
    void deleteClientById(Long id);
    ClientDTO getClientDTOById(Long id);
}