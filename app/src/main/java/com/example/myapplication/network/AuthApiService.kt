package com.example.myapplication.network


import com.example.myapplication.model.AuthRequest
import com.example.myapplication.model.AuthResponse
import com.example.myapplication.model.ScheduleResponse

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApiService {
    @POST("auth/local/signin")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>

    @POST("auth/local/signup")
    suspend fun signup(@Body request: AuthRequest): Response<AuthResponse>
    @GET("/schedule/day")
    suspend fun getScheduleByDay(@Query("date") day: String):List<ScheduleResponse>
}
