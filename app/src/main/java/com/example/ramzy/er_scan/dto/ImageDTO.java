package com.example.ramzy.er_scan.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageDTO {
    @SerializedName("image_name")
    @Expose
    private String image_name;

    public String getImage_name() {
        return image_name;
    }
}
