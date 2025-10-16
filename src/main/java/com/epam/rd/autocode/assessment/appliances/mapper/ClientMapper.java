package com.epam.rd.autocode.assessment.appliances.mapper;

import com.epam.rd.autocode.assessment.appliances.dto.ClientDTO;
import com.epam.rd.autocode.assessment.appliances.model.Client;

public interface ClientMapper {
    ClientDTO toDTO(Client client);
    Client toEntity(ClientDTO clientDTO);
}