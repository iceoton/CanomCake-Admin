package com.iceoton.canomcakeadmin.medel;

import com.google.gson.annotations.Expose;

import org.parceler.Parcel;

@Parcel
public class Admin {
    @Expose
    String id;
    @Expose
    String username;
    @Expose
    String email;
    @Expose
    String fullname;

    public Admin() {
        /*Required empty bean constructor*/
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getFullname() {
        return fullname;
    }
}
