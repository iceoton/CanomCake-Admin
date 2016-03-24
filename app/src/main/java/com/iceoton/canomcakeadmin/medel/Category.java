package com.iceoton.canomcakeadmin.medel;

import com.google.gson.annotations.Expose;

public class Category {
    @Expose
    int id;
    @Expose
    String name_th;
    @Expose
    String image;

    public int getId() {
        return id;
    }

    public String getNameThai() {
        return name_th;
    }

    public String getImageUrl() {
        return image;
    }
}
