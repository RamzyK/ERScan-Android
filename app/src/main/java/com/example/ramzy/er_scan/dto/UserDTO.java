package com.example.ramzy.er_scan.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDTO {
    @SerializedName("_id")
    @Expose
    private String _id;

    @SerializedName("firstname")
    @Expose
    private String firstname;

    @SerializedName("lastname")
    @Expose
    private String lastname;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("createdAt")
    @Expose
    private String createdAt;

    @SerializedName("lastUpdateAt")
    @Expose
    private String lastUpdateAt;

    @SerializedName("imageID")
    @Expose
    private String imageID;

    @SerializedName("companyID")
    @Expose
    private String companyID;

    @SerializedName("id_company")
    @Expose
    private String id_company;

    @SerializedName("isOwner")
    @Expose
    private Boolean isOwner;

    @SerializedName("isStaff")
    @Expose
    private Boolean isStaff;

    @SerializedName("isDeleted")
    @Expose
    private Boolean isDeleted;

    @SerializedName("isAccepted")
    @Expose
    private Boolean isAccepted;

    @SerializedName("token")
    @Expose
    private String token;


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getLastUpdateAt() {
        return lastUpdateAt;
    }

    public void setLastUpdateAt(String lastUpdateAt) {
        this.lastUpdateAt = lastUpdateAt;
    }

    public String getImageID() {
        return imageID;
    }

    public void setImageID(String imageID) {
        this.imageID = imageID;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getId_company() {
        return id_company;
    }

    public void setId_company(String id_company) {
        this.id_company = id_company;
    }

    public Boolean getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(Boolean isOwner) {
        this.isOwner = isOwner;
    }

    public Boolean getIsStaff() {
        return isStaff;
    }

    public void setIsStaff(Boolean isStaff) {
        this.isStaff = isStaff;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Boolean getIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(Boolean isAccepted) {
        this.isAccepted = isAccepted;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
