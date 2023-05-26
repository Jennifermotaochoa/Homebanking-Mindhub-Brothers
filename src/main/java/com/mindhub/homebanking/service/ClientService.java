package com.mindhub.homebanking.service;

import com.mindhub.homebanking.DTOs.ClientDTO;
import com.mindhub.homebanking.models.Client;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface ClientService {
    List<ClientDTO> getClients();
    ClientDTO getClient(Long id);
    ClientDTO getCurrentClient(Authentication authentication);
    void saveClient(Client client);
    Client findByEmail(String email);
}
