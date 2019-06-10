package com.example.ramzy.er_scan.services;

import com.example.ramzy.er_scan.dto.LoginDTO;
import com.example.ramzy.er_scan.dto.UserDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {

    @POST("users/userSignin")
    Call<UserDTO> login(@Body LoginDTO login);
}
