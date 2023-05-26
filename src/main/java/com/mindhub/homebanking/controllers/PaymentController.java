package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.DTOs.PaymentDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.CardService;
import com.mindhub.homebanking.service.ClientService;
import com.mindhub.homebanking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api")
public class PaymentController {
    @Autowired
    private CardService cardService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private TransactionService transactionService;

    @Transactional
    @PostMapping("/payments")
    public ResponseEntity<Object> payments(@RequestBody PaymentDTO paymentDTO){

        Card card = cardService.findByNumber(paymentDTO.getNumber());
        Client client = card.getClient();
        List<Account> accounts = client.getAccounts().stream().collect(Collectors.toList());
        List<Account> accountsBalance = accounts.stream().filter(account -> account.getBalance() > paymentDTO.getAmount()).collect(Collectors.toList());

        Account account = accountsBalance.stream().findFirst().orElse(null);

        if(paymentDTO.getNumber().isBlank()){
            return new ResponseEntity<>("You  need to complete the number", HttpStatus.FORBIDDEN);
        }

        if(paymentDTO.getNumber().length() != 19){
            return new ResponseEntity<>("The card number must have 19 digits including spaces.", HttpStatus.FORBIDDEN);
        }

        if(String.valueOf(paymentDTO.getCvv()).isBlank()){
            return new ResponseEntity<>("You need to complete the cvv", HttpStatus.FORBIDDEN);
        }

        if(paymentDTO.getCvv() <= 0 || paymentDTO.getCvv() > 999){
            return new ResponseEntity<>("The cvv must have 3 digits", HttpStatus.FORBIDDEN);
        }

        if(String.valueOf(paymentDTO.getAmount()).isBlank()){
            return new ResponseEntity<>("You need to complete the amount", HttpStatus.FORBIDDEN);
        }

        if(paymentDTO.getAmount() < 1){
            return new ResponseEntity<>("You amount can't be negative", HttpStatus.FORBIDDEN);
        }

        if(paymentDTO.getDescription().isBlank()){
            return new ResponseEntity<>("You need to complete the description", HttpStatus.FORBIDDEN);
        }

        if(paymentDTO == null){
            return new ResponseEntity<>("Data not found.", HttpStatus.FORBIDDEN);
        }

        if(card == null){
            return new ResponseEntity<>("The card doesn't exist", HttpStatus.FORBIDDEN);
        }

        if(account == null){
            return new ResponseEntity<>("The account doesn't have enough money", HttpStatus.FORBIDDEN);
        }

        if(!card.getActive()){
            return new ResponseEntity<>("This card don't active", HttpStatus.FORBIDDEN);
        }

        if(!account.getActive()){
            return new ResponseEntity<>("This account don't active", HttpStatus.FORBIDDEN);
        }

        if(card.getThruDate().isBefore(LocalDate.now()) || card.getThruDate().equals(LocalDate.now())){
            return new ResponseEntity<>("This card is expired", HttpStatus.FORBIDDEN);
        }

        if(card.getCvv() != paymentDTO.getCvv()){
            return new ResponseEntity<>("This cvv is incorrect", HttpStatus.FORBIDDEN);
        }

        Transaction transaction = new Transaction(TransactionType.DEBIT, paymentDTO.getAmount(), paymentDTO.getDescription(), LocalDateTime.now(), account.getBalance() - paymentDTO.getAmount());

        transactionService.saveTransaction(transaction);
        account.addTransaction(transaction);
        transactionService.saveTransaction(transaction);

        account.setBalance(account.getBalance() - paymentDTO.getAmount());

        return new ResponseEntity<>("Payment has been successfully made.", HttpStatus.ACCEPTED);
    }
}
