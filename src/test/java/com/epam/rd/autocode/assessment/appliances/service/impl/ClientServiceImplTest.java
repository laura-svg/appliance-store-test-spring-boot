package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.dto.ClientDTO;
import com.epam.rd.autocode.assessment.appliances.mapper.ClientMapper;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @InjectMocks
    private ClientServiceImpl clientService;

    private Client client;
    private ClientDTO clientDTO;

    @BeforeEach
    void setUp() {
        client = new Client(1L, "John Doe", "john@example.com", "password123", "1234-5678");
        clientDTO = new ClientDTO(1L, "John Doe", "john@example.com", "password123", "1234-5678");
    }

    @Test
    void getClients_ShouldReturnPageOfClients() {
        List<Client> clients = Collections.singletonList(client);
        Page<Client> clientPage = new PageImpl<>(clients);

        when(clientRepository.findAll(any(PageRequest.class))).thenReturn(clientPage);
        when(clientMapper.toDTO(client)).thenReturn(clientDTO);

        Page<ClientDTO> result = clientService.getClients(0, 10, "name", "asc");

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getUserName()).isEqualTo("John Doe");
        verify(clientRepository).findAll(PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name")));
    }

    @Test
    void getClients_WithDescOrder_ShouldReturnPageWithDescOrder() {
        List<Client> clients = Collections.singletonList(client);
        Page<Client> clientPage = new PageImpl<>(clients);

        when(clientRepository.findAll(any(PageRequest.class))).thenReturn(clientPage);
        when(clientMapper.toDTO(client)).thenReturn(clientDTO);

        Page<ClientDTO> result = clientService.getClients(0, 10, "name", "desc");

        verify(clientRepository).findAll(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "name")));
    }

    @Test
    void findById_WhenExists_ShouldReturnClientDTO() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientMapper.toDTO(client)).thenReturn(clientDTO);

        Optional<ClientDTO> result = clientService.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getUserName()).isEqualTo("John Doe");
        verify(clientRepository).findById(1L);
        verify(clientMapper).toDTO(client);
    }

    @Test
    void findById_WhenNotExists_ShouldReturnEmpty() {
        when(clientRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<ClientDTO> result = clientService.findById(999L);

        assertThat(result).isEmpty();
        verify(clientRepository).findById(999L);
        verify(clientMapper, never()).toDTO(any());
    }

    @Test
    void getAllClients_ShouldReturnListOfClients() {
        List<Client> clients = Collections.singletonList(client);
        when(clientRepository.findAll()).thenReturn(clients);
        when(clientMapper.toDTO(client)).thenReturn(clientDTO);

        List<ClientDTO> result = clientService.getAllClients();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUserName()).isEqualTo("John Doe");
        verify(clientRepository).findAll();
    }

    @Test
    void getClientDTOById_WhenExists_ShouldReturnClientDTO() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientMapper.toDTO(client)).thenReturn(clientDTO);

        ClientDTO result = clientService.getClientDTOById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getUserName()).isEqualTo("John Doe");
        verify(clientRepository).findById(1L);
    }

    @Test
    void getClientDTOById_WhenNotExists_ShouldThrowException() {
        when(clientRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clientService.getClientDTOById(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Client not found");
    }

    @Test
    void saveClient_ShouldSaveClient() {
        when(clientMapper.toEntity(clientDTO)).thenReturn(client);
        when(clientRepository.save(client)).thenReturn(client);

        clientService.saveClient(clientDTO);

        verify(clientMapper).toEntity(clientDTO);
        verify(clientRepository).save(client);
    }

    @Test
    void deleteClientById_ShouldDeleteClient() {
        doNothing().when(clientRepository).deleteById(1L);

        clientService.deleteClientById(1L);

        verify(clientRepository).deleteById(1L);
    }
}