package com.example.ramzy.er_scan.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExpenseReportResponseDTO {
    @SerializedName("er")
    @Expose
    private ExpenseReportDTO[] erList;

    @SerializedName("error")
    private String error;


    public ExpenseReportDTO[] getErList() {
        if(erList == null){
            return new ExpenseReportDTO[0];
        }
        return erList;
    }

    public String getError() {
        return error;
    }
}
