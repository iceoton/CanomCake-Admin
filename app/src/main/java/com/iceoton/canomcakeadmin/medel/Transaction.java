package com.iceoton.canomcakeadmin.medel;

import android.os.Bundle;

import com.google.gson.annotations.Expose;

public class Transaction {
    @Expose
    int id;
    @Expose
    int order_id;
    @Expose
    int customer_id;
    @Expose
    double amount;
    @Expose
    String bank_account;
    @Expose
    String bank_name;
    @Expose
    String description;
    @Expose
    String date_time;

    public int getId() {
        return id;
    }

    public int getOrderId() {
        return order_id;
    }

    public int getCustomerId() {
        return customer_id;
    }

    public double getAmount() {
        return amount;
    }

    public String getBankAccount() {
        return bank_account;
    }

    public String getBankName() {
        return bank_name;
    }

    public String getDescription() {
        return description;
    }

    public String getDateTime() {
        return date_time;
    }

    public Bundle parseToBundle(){
        Bundle bundle = new Bundle();
        bundle.putInt("id", this.id);
        bundle.putInt("order_id", this.order_id);
        bundle.putInt("customer_id", this.customer_id);
        bundle.putDouble("amount", this.amount);
        bundle.putString("bank_account", this.bank_account);
        bundle.putString("bank_name", this.bank_name);
        bundle.putString("description",this.description);
        bundle.putString("date_time", this.date_time);

        return bundle;
    }

    public static Transaction formBundle(Bundle bundle){
        Transaction transaction = new Transaction();
        transaction.id = bundle.getInt("id");
        transaction.order_id = bundle.getInt("order_id");
        transaction.customer_id = bundle.getInt("customer_id");
        transaction.amount = bundle.getDouble("amount");
        transaction.bank_account = bundle.getString("bank_account");
        transaction.bank_name = bundle.getString("bank_name");
        transaction.description = bundle.getString("description");
        transaction.date_time = bundle.getString("date_time");

        return transaction;
    }
}
