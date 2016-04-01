package com.iceoton.canomcakeadmin.medel.response;

import com.google.gson.annotations.Expose;

public class UploadFileResponse {
    @Expose
    int success;
    @Expose
    int error;
    @Expose
    String error_msg;
    @Expose
    String full_image;

    public int getSuccessValue() {
        return success;
    }

    public int getErrorValue() {
        return error;
    }

    public String getErrorMessage() {
        return error_msg;
    }

    public String getImageUrl(){
        return  full_image;
    }
}
