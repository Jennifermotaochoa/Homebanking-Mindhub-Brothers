package com.mindhub.homebanking.DTOs;

import com.mindhub.homebanking.models.ClientLoan;

public class ClientLoanDTO {
    private long id;
    private long loanId;
    private String typeLoan;
    private double amount;
    private int payments;
    private Boolean active;

    public ClientLoanDTO(ClientLoan clientLoan) {
        this.id = clientLoan.getId();
        this.loanId = clientLoan.getLoan().getId();
        this.typeLoan = clientLoan.getLoan().getName();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();
        this.active = clientLoan.getActive();
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

    public Boolean getActive() {
        return active;
    }
}
