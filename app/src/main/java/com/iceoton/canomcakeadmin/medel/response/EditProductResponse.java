package com.iceoton.canomcakeadmin.medel.response;

import com.google.gson.annotations.Expose;
import com.iceoton.canomcakeadmin.medel.Product;

public class EditProductResponse {
    @Expose
    int success;
    @Expose
    int error;
    @Expose
    String error_msg;
    @Expose
    Product result;

    public int getSuccessValue() {
        return success;
    }

    public int getErrorValue() {
        return error;
    }

    public String getErrorMessage() {
        return error_msg;
    }

    public Product getResult() {
        return result;
    }

}
