package com.example.oruclejava.api;

import com.example.oruclejava.api.mapper.ApiResponse;
import com.example.oruclejava.api.mapper.request.LoginRequest;
import com.example.oruclejava.api.mapper.request.RegisterRequest;
import com.example.oruclejava.api.mapper.response.LoginResponse;
import com.example.oruclejava.api.mapper.response.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthenticationApiService {
    @POST("login")
    Call<LoginResponse> authenticate(@Body LoginRequest request);

    @POST("register")
    Call<RegisterResponse> register(@Body RegisterRequest request);
}
