package com.example.ramzy.er_scan.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BugDTO {

    @SerializedName("content")
    @Expose
    String content;

    @SerializedName("id_user")
    @Expose
    String id_user;

    @SerializedName("type")
    @Expose
    String type;

    public BugDTO(String content, String id, String type){
        this.content = content;
        this.id_user = id;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public String getId_user() {
        return id_user;
    }

    public String getType() {
        return type;
    }
}
