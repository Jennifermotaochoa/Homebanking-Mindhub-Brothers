package com.mindhub.homebanking.DTOs;

import com.mindhub.homebanking.models.ClientLoan;

public class ClientLoanDTO {
    private long id;
    private long loanId;
    private String typeLoan;
    private double amount;
    private int payments;

    public ClientLoanDTO(ClientLoan clientLoan) {
        this.id = clientLoan.getId();
        this.loanId = clientLoan.getLoan().getId();
        this.typeLoan = clientLoan.getTypeLoan();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();
    }

    public long getId() {
        return id;
    }

    public long getLoanId() {
        return loanId;
    }

    public String getTypeLoan() {
        return typeLoan;
    }

    public double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }
}