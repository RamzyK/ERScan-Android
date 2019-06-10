package com.example.ramzy.er_scan.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class LoginDTO {
    @SerializedName("email")
    @Expose
    String email;

    @SerializedName("password")
    @Expose
    String password;

    public LoginDTO(String email, String password){
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
