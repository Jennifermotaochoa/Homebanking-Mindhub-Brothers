package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.ClientService;
import com.mindhub.homebanking.service.PDFGeneratorService;
import com.mindhub.homebanking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private PDFGeneratorService pdfGeneratorService;

    @Transactional
    @PostMapping("/clients/current/transactions")
    public ResponseEntity<Object> newTransaction(Authentication authentication,
                                                  @RequestParam Double amount,
                                                  @RequestParam String description,
                                                  @RequestParam String numberOriginAccount,
                                                  @RequestParam String numberDestinationAccount) {
        Client client = clientService.findByEmail(authentication.getName());
        Account originAccount = accountService.findByNumber(numberOriginAccount);
        Account destinationAccount = accountService.findByNumber(numberDestinationAccount);

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

        Transaction newTransaction = new Transaction(TransactionType.DEBIT, amount, description, LocalDateTime.now(), originAccount.getBalance()- amount);
        transactionService.saveTransaction(newTransaction);
        originAccount.addTransaction(newTransaction);
        transactionService.saveTransaction(newTransaction);

        Transaction newTransaction2 = new Transaction(TransactionType.CREDIT, amount, description, LocalDateTime.now(), destinationAccount.getBalance() + amount);
        transactionService.saveTransaction(newTransaction2);
        destinationAccount.addTransaction(newTransaction2);
        transactionService.saveTransaction(newTransaction2);

        originAccount.setBalance(originAccount.getBalance() - amount);
        destinationAccount.setBalance(destinationAccount.getBalance() + amount);

        return new ResponseEntity<>("Successful transfer",HttpStatus.CREATED);
    }

    @GetMapping("/pdf/transactions")
    public ResponseEntity<Object> getTransactionsByDate(HttpServletResponse response,
                                                        Authentication authentication,
                                                        @RequestParam String accountNumber,
                                                        String start, String end) throws IOException {
        Client client = clientService.findByEmail(authentication.getName());
        Account account = accountService.findByNumber(accountNumber);
        List<Transaction> transactions;

        if (client == null) {
            return new ResponseEntity<>("Client doesn't exist", HttpStatus.FORBIDDEN);
        }

        if (account == null) {
            return new ResponseEntity<>("Account doesn't exist", HttpStatus.FORBIDDEN);
        }

        if (!client.getAccounts().contains(account)) {
            return new ResponseEntity<>("Account doesn't belong to this client", HttpStatus.FORBIDDEN);
        }

        if(start.equals("all") || end.equals("all") || start.isEmpty() || end.isEmpty()){
            transactions = transactionService.getTransactionsByAccount(account);
            this.pdfGeneratorService.export(response, transactions, account, "all", "all");
        } else {
            LocalDateTime startDate = LocalDateTime.parse(start);
            LocalDateTime endDate = LocalDateTime.parse(end);
            transactions = transactionService.getTransactionsByAccountAndCreationDate(account, startDate, endDate);

            response.setContentType("application/pdf");
            DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
            String currentDateTime = dateFormatter.format(new Date());

            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=MB-" + account.getNumber() + "-Transactions.pdf";
            response.setHeader(headerKey, headerValue);
            this.pdfGeneratorService.export(response, transactions, account, start, end);
        }

        return new ResponseEntity<>("PDF created!", HttpStatus.CREATED);
    }
}
