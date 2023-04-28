package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.DTOs.AccountDTO;

import com.mindhub.homebanking.DTOs.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;

import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/clients/current/accounts")
    public List<AccountDTO> getAccounts(Authentication authentication) {
        return clientRepository.findByEmail(authentication.getName())
                .getAccounts()
                .stream()
                .map(account -> new AccountDTO(account))
                .collect(Collectors.toList());
    }

    @RequestMapping("/clients/current/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        Optional<Account> optionalAccount = accountRepository.findById(id);
        return optionalAccount.map(account -> new AccountDTO(account)).orElse(null);
    }

    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> registerAccount(Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName());

        String number;
        do{
            number = "VIN-"+ getStringNumber();
        }while(accountRepository.findByNumber(number) != null);

        if(client.getAccounts().size() >= 2) {
            return new ResponseEntity<>("Yo can't have more than three accounts", HttpStatus.FORBIDDEN);
        }
        Account newAccount = new Account(number, LocalDateTime.now(), 0.00);
        accountRepository.save(newAccount);
        client.addAccount(newAccount);
        clientRepository.save(client);
        accountRepository.save(newAccount);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    int min = 000000;
    int max = 999999;
    private int getNumberRandom(int min, int max){
        Random random = new Random();
        int number = random.nextInt(max) + min;
        return number;
    }
    private String getStringNumber(){
        int numberRandom = getNumberRandom(min, max);
        return String.valueOf(numberRandom);
    }


}
