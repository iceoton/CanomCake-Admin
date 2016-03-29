package com.iceoton.canomcakeadmin.medel.response;

import com.google.gson.annotations.Expose;
import com.iceoton.canomcakeadmin.medel.Transaction;

import java.util.ArrayList;

public class GetAllTransactionsResponse {
    @Expose
    int success;
    @Expose
    int error;
    @Expose
    String error_msg;
    @Expose
    ArrayList<Transaction> result;

    public int getSuccess() {
        return success;
    }

    public int getError() {
        return error;
    }

    public String getErrorMessage() {
        return error_msg;
    }

    public ArrayList<Transaction> getResult() {
        return result;
    }
}
