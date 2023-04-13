package com.mindhub.homebanking.DTOs;

import com.mindhub.homebanking.models.Account;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.Set;

public class AccountDTO {
    private long id;
    private String number;
    private LocalDateTime creationDate;
    private  double balance;
    private Set<TransactionDTO> transactionsDTO;
    public AccountDTO(Account account) {

        this.id = account.getId();

        this.number = account.getNumber();

        this.creationDate = account.getCreationDate();

        this.balance = account.getBalance();

        this.transactionsDTO = account.getTransactions().stream().map(transaction -> new TransactionDTO(transaction)).collect(Collectors.toSet());
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public Set<TransactionDTO> getTransactionsDTO() {
        return transactionsDTO;
    }
}
