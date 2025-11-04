package com.example.myapplication.network


import com.example.myapplication.model.AuthRequest
import com.example.myapplication.model.AuthResponse

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/local/signin")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>

    @POST("auth/local/signup")
    suspend fun signup(@Body request: AuthRequest): Response<AuthResponse>
}
