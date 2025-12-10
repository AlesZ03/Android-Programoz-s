package com.example.myapplication.network


import com.example.myapplication.model.AddHabitRequest
import com.example.myapplication.model.AuthRequest
import com.example.myapplication.model.AuthResponse
import com.example.myapplication.model.HabitResponse
import com.example.myapplication.model.ProfileResponse
import com.example.myapplication.model.ScheduleRequest
import com.example.myapplication.model.ScheduleResponse

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AuthApiService {
    @POST("auth/local/signin")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>

    @POST("auth/local/signup")
    suspend fun signup(@Body request: AuthRequest): Response<AuthResponse>
    @GET("/schedule/day")
    suspend fun getScheduleByDay(@Query("date") day: String):List<ScheduleResponse>
    @POST("/habit")
    suspend fun addHabit(@Body request: AddHabitRequest): HabitResponse
    @POST("schedule/custom")
    suspend fun addSchedule(@Body schedule: ScheduleRequest): Response<Unit>
    @GET("profile")
    suspend fun getProfile(): Response<ProfileResponse>

    @DELETE("schedule/{id}")
    suspend fun deleteSchedule(@Path("id") scheduleId: Long): Response<Unit>
}
