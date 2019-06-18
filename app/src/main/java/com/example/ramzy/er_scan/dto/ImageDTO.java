package com.example.ramzy.er_scan.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageDTO {
    @SerializedName("url")
    @Expose
    private String url;

    public String getUrl() {
        return url;
    }
}
