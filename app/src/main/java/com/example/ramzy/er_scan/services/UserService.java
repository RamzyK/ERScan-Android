package com.example.ramzy.er_scan.services;

import com.example.ramzy.er_scan.dto.LoginDTO;
import com.example.ramzy.er_scan.dto.UserDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface UserService {

    @POST("users/userSignin")
    Call<UserDTO> login(@Body LoginDTO login);

    @PUT("users//updatePass")
    Call<UserDTO> updateUserPassword(@Body LoginDTO userLog, @Header("x-access-token") String token);

    @PUT("users/updateUser")
    Call<UserDTO> updateUserAccountParams(@Body UserDTO userDTO, @Header("x-access-token") String token);

    @POST("users/checkPass")
    Call<Boolean> chekUserPasswordInDb(@Body LoginDTO oldPass, @Header("x-access-token") String token);
}
