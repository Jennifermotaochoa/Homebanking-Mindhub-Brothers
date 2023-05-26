package com.mindhub.homebanking.service;

import com.mindhub.homebanking.models.ClientLoan;

import java.util.Optional;

public interface ClientLoanService {
    Optional<ClientLoan> findById(long id);
    void saveClientLoan(ClientLoan clientLoan);
}
