package com.example.ramzy.er_scan.services;

import com.example.ramzy.er_scan.dto.ExpenseReportDTO;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ErService {

    @POST("expenseReport/create")
    Call<Response<String>> submitExpense(@Body ExpenseReportDTO er, @Header("x-access-token") String token);

}