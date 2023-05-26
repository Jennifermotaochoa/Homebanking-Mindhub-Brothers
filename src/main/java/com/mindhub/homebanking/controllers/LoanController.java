package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.DTOs.CreateLoanDTO;
import com.mindhub.homebanking.DTOs.LoanApplicationDTO;
import com.mindhub.homebanking.DTOs.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private LoanService loanService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private ClientLoanService clientLoanService;

    @GetMapping("/loans")
    public List<LoanDTO> getLoans(Authentication authentication) {
        return loanService.getLoans(authentication);
    }
    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> newLoan(Authentication authentication,
                                          @RequestBody LoanApplicationDTO loanApplicationDTO) {
        Client client = clientService.findByEmail(authentication.getName());
        Loan loan = loanService.findById(loanApplicationDTO.getLoanId()).orElse(null);
        Account account = accountService.findByNumber(loanApplicationDTO.getNumberAccount());
        List<String> loansName = client.getClientLoans().stream().filter(clientLoan1 -> clientLoan1.getActive()).map(clientLoan1 -> clientLoan1.getLoan().getName()).collect(Collectors.toList());

        if (loansName.contains(loan.getName())){
            return new ResponseEntity<>("You already have an active loan", HttpStatus.FORBIDDEN);
        }

        if(loan == null){
            return new ResponseEntity<>("The loan doesn't exist", HttpStatus.FORBIDDEN);
        }

        if(loanApplicationDTO == null){
            return new ResponseEntity<>("Data not found.",HttpStatus.FORBIDDEN);
        }

        if(account == null){
            return new ResponseEntity<>("The destination account doesn't exist",HttpStatus.FORBIDDEN);
        }

        if(loanApplicationDTO.getNumberAccount().isBlank()){
            return new ResponseEntity<>("You need to complete the destination account",HttpStatus.FORBIDDEN);
        }

        if(loanApplicationDTO.getAmount() < 1000){
            return new ResponseEntity<>("Your amount must be > 1000", HttpStatus.FORBIDDEN);
        }

        if (loanApplicationDTO.getPayments() < 1){
            return new ResponseEntity<>("The payments must be greater than 0", HttpStatus.FORBIDDEN);
        }

        if(!client.getAccounts().contains(account)){
            return new ResponseEntity<>("This account isn't authenticated.", HttpStatus.FORBIDDEN);
        }

        if(loanApplicationDTO.getAmount() > loan.getMaxAmount()){
            return new ResponseEntity<>("The requested loan is greater than the one we offer.", HttpStatus.FORBIDDEN);
        }

        if(!loan.getPayments().contains(loanApplicationDTO.getPayments())){
            return new ResponseEntity<>("The payments doesn't available", HttpStatus.FORBIDDEN);
        }

        double loanInterest = loanApplicationDTO.getAmount() * loan.getInterest() + loanApplicationDTO.getAmount();
        //double loanPayments = Math.floor(loanInterest / loanApplicationDTO.getPayments());

        ClientLoan clientLoan = new ClientLoan(loanInterest, loanApplicationDTO.getPayments(), true);
        Transaction transactionLoan = new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(), loan.getName() + " approved", LocalDateTime.now(), account.getBalance() + loanApplicationDTO.getAmount());

        account.setBalance(loanApplicationDTO.getAmount() + account.getBalance());

        clientLoanService.saveClientLoan(clientLoan);
        transactionService.saveTransaction(transactionLoan);

        client.addClientLoan(clientLoan);
        account.addTransaction(transactionLoan);
        loan.addClientLoan(clientLoan);

        clientLoanService.saveClientLoan(clientLoan);
        transactionService.saveTransaction(transactionLoan);

        return new ResponseEntity<>("Successful loan", HttpStatus.CREATED);
    }
    @PostMapping("/loans/admin")
    public ResponseEntity<Object> createLoan(Authentication authentication,
                                             @RequestBody CreateLoanDTO createLoanDTO){
        Client client = clientService.findByEmail(authentication.getName());
        String name = createLoanDTO.getName().toUpperCase();
        Loan usedLoan = loanService.findByName(name);


        if(!client.getEmail().contains("jennifer@hotmail.com")){
            return new ResponseEntity<>("You don't have authorization", HttpStatus.FORBIDDEN);
        }

        if (name.isBlank()){
            return new ResponseEntity<>("You need to complete the name", HttpStatus.FORBIDDEN);
        }

        if(String.valueOf(createLoanDTO.getPayments()).isBlank()){
            return new ResponseEntity<>("You need to complete the payments", HttpStatus.FORBIDDEN);
        }

        if (String.valueOf(createLoanDTO.getAmount()).isBlank()){
            return new ResponseEntity<>("You need to complete the amount", HttpStatus.FORBIDDEN);
        }

        if (createLoanDTO.getAmount() < 1){
            return new ResponseEntity<>("The requested loan is greater than the one we offer.", HttpStatus.FORBIDDEN);
        }

        if(createLoanDTO.getPayments().size() < 1){
            return new ResponseEntity<>("The payments must be greater than 1.",HttpStatus.FORBIDDEN);
        }

        if(usedLoan != null){
            return new ResponseEntity<>("You can't have the same loan", HttpStatus.FORBIDDEN);
        }

        Loan createLoan = new Loan(createLoanDTO.getName(), createLoanDTO.getAmount(), createLoanDTO.getPayments(), createLoanDTO.getInterest());
        loanService.saveLoan(createLoan);

        return new ResponseEntity<>("Successful loan", HttpStatus.CREATED);
    }

    @Transactional
    @PostMapping("/loans/pay")
    public ResponseEntity<Object> payLoan(Authentication authentication,
                                          @RequestParam long loanId,
                                          @RequestParam double amount,
                                          @RequestParam String account){
        Client client = clientService.findByEmail(authentication.getName());

        ClientLoan clientLoan = client.getClientLoans().stream().filter(clientLoan1 -> clientLoan1.getLoan().getId() == loanId).findFirst().orElse(null);
        Account currentAccount = accountService.findByNumber(account);

        if(currentAccount == null){
            return new ResponseEntity<>("This account don't exist", HttpStatus.FORBIDDEN);
        }

        if(currentAccount.getClient() != client){
            return new ResponseEntity<>("This account don't is your", HttpStatus.FORBIDDEN);
        }

        if(clientLoan == null){
            return new ResponseEntity<>("This loan don't exist", HttpStatus.FORBIDDEN);
        }

        if(amount < 1){
            return new ResponseEntity<>("You amount can't be negative", HttpStatus.FORBIDDEN);
        }

        if(amount == clientLoan.getAmount()){
            clientLoan.setActive(false);
        }

        currentAccount.setBalance(currentAccount.getBalance() - amount);
        clientLoan.setAmount(clientLoan.getAmount() - amount);

        Transaction transaction = new Transaction(TransactionType.DEBIT, amount, clientLoan.getLoan().getName() + " payment", LocalDateTime.now(), currentAccount.getBalance());

        transactionService.saveTransaction(transaction);

        currentAccount.addTransaction(transaction);


        transactionService.saveTransaction(transaction);

        if(amount < clientLoan.getAmount()){
            clientLoan.setPayments(clientLoan.getPayments() - 1);
        }
        else {
            clientLoan.setPayments(0);
        }

        return new ResponseEntity<>("Successful payment", HttpStatus.CREATED);
    }
}
