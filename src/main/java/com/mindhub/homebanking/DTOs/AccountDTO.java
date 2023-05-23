package com.mindhub.homebanking.DTOs;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.Set;

public class AccountDTO {
    private long id;
    private String number;
    private LocalDateTime creationDate;
    private  double balance;
    private Set<TransactionDTO> transactionsDTO;
    private Boolean active;
    private AccountType accountType;

    public AccountDTO(Account account) {

        this.id = account.getId();

        this.number = account.getNumber();

        this.creationDate = account.getCreationDate();

        this.balance = account.getBalance();

        this.transactionsDTO = account.getTransactions().stream().map(transaction -> new TransactionDTO(transaction)).collect(Collectors.toSet());

        this.active = account.getActive();

        this.accountType = account.getAccountType();
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

    public Boolean getActive() {
        return active;
    }

    public AccountType getAccountType() {
        return accountType;
    }
}
