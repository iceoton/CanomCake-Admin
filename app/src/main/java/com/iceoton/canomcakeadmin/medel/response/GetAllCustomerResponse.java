package com.iceoton.canomcakeadmin.medel.response;


import com.google.gson.annotations.Expose;
import com.iceoton.canomcakeadmin.medel.User;

import java.util.ArrayList;

public class GetAllCustomerResponse {
    @Expose
    int success;
    @Expose
    int error;
    @Expose
    String error_msg;
    @Expose
    ArrayList<User> result;

    public int getSuccessValue() {
        return success;
    }

    public int getErrorValue() {
        return error;
    }

    public String getErrorMessage() {
        return error_msg;
    }

    public ArrayList<User> getResult() {
        return result;
    }
}
