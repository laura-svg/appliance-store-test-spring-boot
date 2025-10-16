package com.epam.rd.autocode.assessment.appliances.mapper.impl;

import com.epam.rd.autocode.assessment.appliances.dto.ClientDTO;
import com.epam.rd.autocode.assessment.appliances.mapper.ClientMapper;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientMapperImpl implements ClientMapper {
    @Override
    public ClientDTO toDTO(Client client) {
        if (client == null) {
            return null;
        }
        return new ClientDTO(
                client.getId(),
                client.getName(),
                client.getEmail(),
                client.getPassword(),
                client.getCard()
        );
    }
    @Override
    public Client toEntity(ClientDTO clientDTO) {
        if (clientDTO == null) {
            return null;
        }
        return new Client(
                clientDTO.getId(),
                clientDTO.getUserName(),
                clientDTO.getUserEmail(),
                clientDTO.getUserPassword(),
                clientDTO.getClientCard()
        );
    }
}