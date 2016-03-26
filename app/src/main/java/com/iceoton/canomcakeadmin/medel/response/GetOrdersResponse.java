package com.iceoton.canomcakeadmin.medel.response;

import com.google.gson.annotations.Expose;
import com.iceoton.canomcakeadmin.medel.Order;

import java.util.ArrayList;

public class GetOrdersResponse {
    @Expose
    int success;
    @Expose
    int error;
    @Expose
    String error_msg;
    @Expose
    ArrayList<Order> result;

    public int getSuccessValue() {
        return success;
    }

    public int getErrorValue() {
        return error;
    }

    public String getErrorMessage() {
        return error_msg;
    }

    public ArrayList<Order> getResult() {
        return result;
    }
}
