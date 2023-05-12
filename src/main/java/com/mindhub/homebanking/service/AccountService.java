package com.mindhub.homebanking.service;

import com.mindhub.homebanking.DTOs.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface AccountService {
    List<AccountDTO> getAccounts(Authentication authentication);
    AccountDTO getAccount(Long id);
    void saveAccount(Account account);
    Account findByNumber(String number);
}
