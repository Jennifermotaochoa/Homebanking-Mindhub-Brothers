package com.mindhub.homebanking.service;

import com.mindhub.homebanking.DTOs.LoanDTO;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface LoanService {
    List<LoanDTO> getLoans(Authentication authentication);
    Optional<Loan> findById(long id);
    void saveLoan(Loan loan);
    Loan findByName(String name);
}
