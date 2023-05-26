package com.mindhub.homebanking.DTOs;

public class PaymentDTO {
    private String number;
    private int cvv;
    private double amount;
    private String description;

    public PaymentDTO(String number, int cvv, double amount, String description) {
        this.number = number;
        this.cvv = cvv;
        this.amount = amount;
        this.description = description;
    }

    public String getNumber() {
        return number;
    }

    public int getCvv() {
        return cvv;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }
}
