package com.mindhub.homebanking.service;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {
    void saveTransaction(Transaction transaction);
    List<Transaction> getTransactionsByAccount(Account account);
    List<Transaction> getTransactionsByAccountAndCreationDate(Account account, LocalDateTime startDate, LocalDateTime endDate);
}
