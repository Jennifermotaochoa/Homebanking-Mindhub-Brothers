package com.mindhub.homebanking.DTOs;

import java.util.List;

public class CreateLoanDTO {
    private String name;
    private double amount;
    private List<Integer> payments;
    private Double interest;

    public CreateLoanDTO(String name, double amount, List<Integer> payments, Double interest) {
        this.name = name;
        this.amount = amount;
        this.payments = payments;
        this.interest = interest;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public Double getInterest() {
        return interest;
    }
}
