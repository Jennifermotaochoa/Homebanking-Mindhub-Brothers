package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.DTOs.LoanApplicationDTO;
import com.mindhub.homebanking.DTOs.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
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

    @RequestMapping("/loans")
    public List<LoanDTO> getLoans(Authentication authentication) {
        return loanService.getLoans(authentication);
    }
    @Transactional
    @RequestMapping(path = "/loans", method = RequestMethod.POST)
    public ResponseEntity<Object> newLoan(Authentication authentication,
                                          @RequestBody LoanApplicationDTO loanApplicationDTO) {
        Client client = clientService.findByEmail(authentication.getName());
        Loan loan = loanService.findById(loanApplicationDTO.getLoanId()).orElse(null);
        Account account = accountService.findByNumber(loanApplicationDTO.getNumberAccount());
        ClientLoan clientLoanId = clientLoanService.findById(loan.getId()).orElse(null);

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

        if(String.valueOf(loanApplicationDTO.getAmount()).isBlank()){
            return new ResponseEntity<>("You need to complete the amount", HttpStatus.FORBIDDEN);
        }

        if (loanApplicationDTO.getPayments() < 1){
            return new ResponseEntity<>("The payments must be greater than 0", HttpStatus.FORBIDDEN);
        }

        if(!client.getAccounts().contains(account)){
            return new ResponseEntity<>("This account isn't authenticated", HttpStatus.FORBIDDEN);
        }

        if(loanApplicationDTO.getAmount() > loan.getMaxAmount()){
            return new ResponseEntity<>("The loan solicited is mayor", HttpStatus.FORBIDDEN);
        }

        if(!loan.getPayments().contains(loanApplicationDTO.getPayments())){
            return new ResponseEntity<>("The payments doesn't available", HttpStatus.FORBIDDEN);
        }

        if(clientLoanId != null){
            return new ResponseEntity<>("You can't apply for the same loan", HttpStatus.FORBIDDEN);
        }


        double loanInterest = loanApplicationDTO.getAmount() * 0.2 + loanApplicationDTO.getAmount();
        //double loanPayments = Math.floor(loanInterest / loanApplicationDTO.getPayments());

        ClientLoan clientLoan = new ClientLoan(loanInterest, loanApplicationDTO.getPayments());
        Transaction transactionLoan = new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(), loan.getName() + " approved", LocalDateTime.now());

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
}
