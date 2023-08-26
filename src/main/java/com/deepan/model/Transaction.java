package com.deepan.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class Transaction {

    @Id
    private String tid;
    private double amount;
    private LocalDate date;
    private String ttype;

    public Transaction(String tid, double amount, LocalDate date, String ttype) {
        this.tid = tid;
        this.amount = amount;
        this.date = date;
        this.ttype = ttype;
    }

    public Transaction(){}

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTtype() {
        return ttype;
    }

    public void setTtype(String ttype) {
        this.ttype = ttype;
    }

    @Override
    public int hashCode() {
        return tid.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Transaction){

            Transaction t = (Transaction) obj;

            if(t.tid.equals(this.tid)){
                return true;
            } else{
                return false;
            }

        } else{
            return false;
        }
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "tid='" + tid + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                ", ttype='" + ttype + '\'' +
                '}';
    }
}
