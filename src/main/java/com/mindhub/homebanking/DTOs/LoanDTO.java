package com.mindhub.homebanking.DTOs;

import com.mindhub.homebanking.models.Loan;

import java.util.List;

public class LoanDTO {
    private long id;
    private String name;
    private Double amount;
    private List<Integer> payments;

    public LoanDTO(Loan loan) {
        this.id = loan.getId();
        this.name = loan.getName();
        this.amount = loan.getMaxAmount();
        this.payments = loan.getPayments();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }
}
