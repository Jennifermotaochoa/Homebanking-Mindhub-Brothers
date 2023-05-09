package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Transactional
    @RequestMapping(path = "/clients/current/transactions", method = RequestMethod.POST)
    public ResponseEntity<Object> newTransaction(Authentication authentication,
                                                  @RequestParam Double amount,
                                                  @RequestParam String description,
                                                  @RequestParam String numberOriginAccount,
                                                  @RequestParam String numberDestinationAccount) {
        Client client = clientRepository.findByEmail(authentication.getName());
        Account originAccount = accountRepository.findByNumber(numberOriginAccount);
        Account destinationAccount = accountRepository.findByNumber(numberDestinationAccount);

        if(originAccount == null){
            return new ResponseEntity<>("The origin account doesn't exist", HttpStatus.FORBIDDEN);
        }

        if(destinationAccount == null){
            return new ResponseEntity<>("The destination account doesn't exist", HttpStatus.FORBIDDEN);
        }

        if (numberDestinationAccount.isBlank()){
            return new ResponseEntity<>("You need to complete the account destination", HttpStatus.FORBIDDEN);
        }

        if (description.isBlank()){
            return new ResponseEntity<>("You need to complete the description", HttpStatus.FORBIDDEN);
        }

        if (numberOriginAccount.isBlank()){
            return new ResponseEntity<>("You need to complete the account origin", HttpStatus.FORBIDDEN);
        }

        if(amount < 1){
            return new ResponseEntity<>("You amount can't be negative", HttpStatus.FORBIDDEN);
        }

        if(originAccount.getBalance() < amount){
            return new ResponseEntity<>("You haven't enough balance", HttpStatus.FORBIDDEN);
        }

        if(numberOriginAccount.equals(numberDestinationAccount)){
            return new ResponseEntity<>("You can't send money to the same account", HttpStatus.FORBIDDEN);
        }

        if(client.getAccounts().stream().filter(account -> account.getNumber().equalsIgnoreCase(numberOriginAccount)).collect(Collectors.toList()).size() == 0){
            return new ResponseEntity<>("This account isn't yours", HttpStatus.FORBIDDEN);
        }

        originAccount.setBalance(originAccount.getBalance() - amount);
        destinationAccount.setBalance(destinationAccount.getBalance() + amount);

        Transaction newTransaction = new Transaction(TransactionType.DEBIT, amount, description, LocalDateTime.now());
        transactionRepository.save(newTransaction);
        originAccount.addTransaction(newTransaction);
        transactionRepository.save(newTransaction);

        Transaction newTransaction2 = new Transaction(TransactionType.CREDIT, amount, description, LocalDateTime.now());
        transactionRepository.save(newTransaction2);
        destinationAccount.addTransaction(newTransaction2);
        transactionRepository.save(newTransaction2);

        return new ResponseEntity<>("Successful transfer",HttpStatus.CREATED);
    }
}
