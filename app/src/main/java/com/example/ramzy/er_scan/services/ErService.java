package com.example.ramzy.er_scan.services;

import com.example.ramzy.er_scan.dto.ExpenseReportDTO;
import com.example.ramzy.er_scan.dto.ExpenseReportResponseDTO;
import com.example.ramzy.er_scan.dto.ImageDTO;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;

import retrofit2.http.Part;

public interface ErService {

    @POST("expenseReport/create")
    Call<Response<String>> submitExpense(@Body ExpenseReportDTO er, @Header("x-access-token") String token);

    @GET("expenseReport/erByUser")
    Call<ExpenseReportResponseDTO> getUserExpenseReports(@Header("x-access-token") String token);

    @GET("expenseReport/lastThree")
    Call<ExpenseReportResponseDTO> getLast3Expenses(@Header("x-access-token") String token);


    @Multipart
    @POST("images/upload")
    Call<ImageDTO> upload(
            @Part("description") RequestBody description,
            @Part MultipartBody.Part file
    );

}
