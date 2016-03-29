package com.iceoton.canomcakeadmin.medel;

import com.google.gson.annotations.Expose;

public class Dashboard {
    @Expose
    double total_sold_month;
    @Expose
    int count_waiting_order;
    @Expose
    int count_transactions;
    @Expose
    int count_products;
    @Expose
    int count_categories;
    @Expose
    int count_customers;
    @Expose
    int count_admin;
    @Expose
    Product best_seller;

    public double getMontCirculation() {
        return total_sold_month;
    }

    public int getNumberOfWaitingOrder() {
        return count_waiting_order;
    }

    public int getNumberOfTransactions() {
        return count_transactions;
    }

    public int getNumberOfProducts() {
        return count_products;
    }

    public int getNumberOfCategories() {
        return count_categories;
    }

    public int getNumberOfCustomers() {
        return count_customers;
    }

    public int getNumberOfAdmin() {
        return count_admin;
    }

    public Product getBestSeller() {
        return best_seller;
    }
}
