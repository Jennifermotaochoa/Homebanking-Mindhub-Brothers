package com.mindhub.homebanking.service.implement;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImplement implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getTransactionsByAccount(Account account) {
        return transactionRepository.findAllByAccount(account);
    }

    @Override
    public List<Transaction> getTransactionsByAccountAndCreationDate(Account account, LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findAllByAccountAndCreationDateBetween(account, startDate, endDate);
    }
}
