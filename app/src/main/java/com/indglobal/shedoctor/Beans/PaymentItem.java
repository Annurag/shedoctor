package com.indglobal.shedoctor.Beans;

/**
 * Created by SONY on 30/09/2016.
 */
public class PaymentItem {

    String  date, amount, transaction_id,status,remarks;

    public PaymentItem(String date, String amount, String transaction_id, String status, String remarks) {
        this.date = date;
        this.amount = amount;
        this.transaction_id = transaction_id;
        this.status = status;
        this.remarks = remarks;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
