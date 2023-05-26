package com.mindhub.homebanking.DTOs;

public class LoanApplicationDTO {
    private long loanId;
    private double amount;
    private Integer payments;
    private String numberAccount;

    public LoanApplicationDTO(long loanId, double amount, Integer payments, String numberAccount) {
        this.loanId = loanId;
        this.amount = amount;
        this.payments = payments;
        this.numberAccount = numberAccount;
    }

    public long getLoanId() {
        return loanId;
    }


    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Integer getPayments() {
        return payments;
    }

    public void setPayments(Integer payments) {
        this.payments = payments;
    }

    public String getNumberAccount() {
        return numberAccount;
    }

    public void setNumberAccount(String numberAccount) {
        this.numberAccount = numberAccount;
    }
}
