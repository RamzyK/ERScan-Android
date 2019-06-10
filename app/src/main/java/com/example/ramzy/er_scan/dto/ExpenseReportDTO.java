package com.example.ramzy.er_scan.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExpenseReportDTO {

    @SerializedName("price")
    @Expose
    int price;

    @SerializedName("vat")
    @Expose
    int vat;

    @SerializedName("address")
    @Expose
    String address;

    public ExpenseReportDTO(int p, int v, String address){
        this.price = p;
        this.vat = v;
        this.address = address;
    }

    public int getPrice() {
        return price;
    }

    public int getVat() {
        return vat;
    }

    public String getAddress() {
        return address;
    }
}
