package com.example.ramzy.er_scan.services;

import com.example.ramzy.er_scan.dto.BugDTO;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface BugService {

    @POST("bugs/signalBug")
    Call<Response<String>> submitBug(@Body BugDTO bug, @Header("x-access-token") String token);
}
