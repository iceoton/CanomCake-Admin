package com.iceoton.canomcakeadmin.medel;


import com.google.gson.annotations.Expose;

import org.parceler.Parcel;

@Parcel
public class User {
    @Expose
    String id;
    @Expose
    String username;
    @Expose
    String email;
    @Expose
    String fullname;
    @Expose
    String address;
    @Expose
    String phone_number;
    @Expose
    String latitude;
    @Expose
    String longitude;
    @Expose
    String active;

    public User() {
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

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phone_number;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public boolean isActive() {
        return active.equals("1");
    }
}
