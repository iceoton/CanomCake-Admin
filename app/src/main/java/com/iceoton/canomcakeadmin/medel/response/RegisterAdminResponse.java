package com.iceoton.canomcakeadmin.medel.response;

import com.google.gson.annotations.Expose;
import com.iceoton.canomcakeadmin.medel.Admin;

public class RegisterAdminResponse {
    @Expose
    int success;
    @Expose
    int error;
    @Expose
    String error_msg;
    @Expose
    Admin result;

    public int getSuccessValue() {
        return success;
    }

    public int getErrorValue() {
        return error;
    }

    public String getErrorMessage() {
        return error_msg;
    }

    public Admin getResult() {
        return result;
    }

}
