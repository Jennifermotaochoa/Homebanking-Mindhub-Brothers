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
    Set<TransactionDTO> transactionsDTO;
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

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Set<TransactionDTO> getTransactionsDTO() {
        return transactionsDTO;
    }

    public void setTransactionsDTO(Set<TransactionDTO> transactionsDTO) {
        this.transactionsDTO = transactionsDTO;
    }
}
