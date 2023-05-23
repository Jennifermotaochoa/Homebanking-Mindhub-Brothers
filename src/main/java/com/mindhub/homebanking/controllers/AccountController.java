package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.DTOs.AccountDTO;

import com.mindhub.homebanking.DTOs.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;

import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.ClientService;
import com.mindhub.homebanking.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;

    @GetMapping("/clients/current/accounts")
    public List<AccountDTO> getAccounts(Authentication authentication) {
        return accountService.getAccounts(authentication);
    }

    @GetMapping("/clients/current/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return accountService.getAccount(id);
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> registerAccount(Authentication authentication,
                                                  @RequestParam AccountType accountType){
        Client client = clientService.findByEmail(authentication.getName());
        Set<Account> accounts = client.getAccounts().stream().filter(account -> account.getActive()).collect(Collectors.toSet());

        String number;
        do{
            number = "VIN-"+ AccountUtils.getStringNumber();
        }while(accountService.findByNumber(number) != null);

        if(accounts.size() == 3) {
            return new ResponseEntity<>("Yo can't have more than three accounts", HttpStatus.FORBIDDEN);
        }

        Account newAccount = new Account(number, LocalDateTime.now(), 0.00, true, accountType);
        accountService.saveAccount(newAccount);
        client.addAccount(newAccount);
        clientService.saveClient(client);
        accountService.saveAccount(newAccount);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @PutMapping("/clients/current/accounts/{id}")
    public ResponseEntity<Object> deleteAccount(Authentication authentication, @PathVariable Long id){
        Client client = clientService.findByEmail(authentication.getName());
        Account selectAccount = accountService.findById(id).orElse(null);

        if(!selectAccount.getActive()){
            return new ResponseEntity<>("This account isn't active", HttpStatus.FORBIDDEN);
        }

        if(!client.getAccounts().contains(selectAccount)){
            return new ResponseEntity<>("This account isn't yours", HttpStatus.FORBIDDEN);
        }

        if(selectAccount == null){
            return new ResponseEntity<>("This account doesn't exist", HttpStatus.FORBIDDEN);
        }

        if(selectAccount.getBalance() > 0.0){
            return new ResponseEntity<>("This account have money", HttpStatus.FORBIDDEN);
        }

        selectAccount.setActive(false);
        accountService.saveAccount(selectAccount);

        return new ResponseEntity<>("Delete account", HttpStatus.ACCEPTED);
    }
}
