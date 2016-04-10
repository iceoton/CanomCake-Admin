package com.iceoton.canomcakeadmin.medel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Product {
    @Expose
    String code;
    @Expose
    String category_id;
    @Expose
    String name_th;
    @Expose
    String name_en;
    @Expose
    String detail;
    @Expose
    String price;
    @Expose
    String unit;
    @Expose
    String image;
    @SerializedName("available")
    int available;

    public Product() {
        /*Required empty bean constructor*/
    }

    public String getCode() {
        return code;
    }

    public int getCategoryId() {
        return Integer.parseInt(category_id);
    }

    public String getNameThai() {
        return name_th;
    }

    public String getNameEnglish() {
        return name_en;
    }

    public String getDetail() {
        return detail;
    }

    public double getPrice() {
        return Double.parseDouble(price);
    }

    public String getUnit() {
        return unit;
    }

    public String getImageUrl() {
        return image;
    }

    public void setCategoryId(String categoryId) {
        this.category_id = categoryId;
    }

    public void setNameThai(String name) {
        this.name_th = name;
    }

    public void setNameEnglish(String name) {
        this.name_en = name;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setImageUrl(String imageUrl) {
        this.image = imageUrl;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }
}
