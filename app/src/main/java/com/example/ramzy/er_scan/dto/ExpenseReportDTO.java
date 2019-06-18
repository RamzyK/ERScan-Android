package com.example.ramzy.er_scan.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExpenseReportDTO implements Parcelable {

    @SerializedName("price")
    @Expose
    int price;

    @SerializedName("vat")
    @Expose
    int vat;

    @SerializedName("address")
    @Expose
    String address;

    @SerializedName("imageID")
    @Expose
    String imageID;

    public ExpenseReportDTO(int p, int v, String address, String image_url){
        this.price = p;
        this.vat = v;
        this.address = address;
        this.imageID = image_url;
    }

    protected ExpenseReportDTO(Parcel in) {
        price = in.readInt();
        vat = in.readInt();
        address = in.readString();
        imageID = in.readString();
    }

    public static final Creator<ExpenseReportDTO> CREATOR = new Creator<ExpenseReportDTO>() {
        @Override
        public ExpenseReportDTO createFromParcel(Parcel in) {
            return new ExpenseReportDTO(in);
        }

        @Override
        public ExpenseReportDTO[] newArray(int size) {
            return new ExpenseReportDTO[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(price);
        dest.writeInt(vat);
        dest.writeString(address);
        dest.writeString(imageID);
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

    public String getImageID() {
        return imageID;
    }

    public void setImageID(String imageID) {
        this.imageID = imageID;
    }
}
