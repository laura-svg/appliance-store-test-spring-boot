package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import com.epam.rd.autocode.assessment.appliances.dto.ClientDTO;
import com.epam.rd.autocode.assessment.appliances.mapper.ClientMapper;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.repository.ClientRepository;
import com.epam.rd.autocode.assessment.appliances.service.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@AllArgsConstructor
@Service
public class ClientServiceImpl implements ClientService {
   private ClientRepository clientRepository;
   private ClientMapper clientMapper;

    @Override
    @Loggable
    public Page<ClientDTO> getClients(int page, int size, String sort, String order) {
        Sort.Direction direction = order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sort));
        return clientRepository.findAll(pageRequest)
                .map(clientMapper::toDTO);
    }
    @Override
    @Loggable
    public Optional<ClientDTO> findById(Long id) {
        return clientRepository.findById(id)
                .map(clientMapper::toDTO);
    }
    @Override
    @Loggable
    public List<ClientDTO> getAllClients() {
        return clientRepository.findAll()
                .stream()
                .map(clientMapper::toDTO)
                .collect(Collectors.toList());
    }
    public ClientDTO getClientDTOById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        return clientMapper.toDTO(client);
    }
    public void saveClient(ClientDTO clientDTO) {
        clientRepository.save(clientMapper.toEntity(clientDTO));
    }
    public void deleteClientById(Long id) {
        clientRepository.deleteById(id);
    }
}