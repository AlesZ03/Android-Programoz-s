package com.example.myapplication.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.myapplication.network.AuthApiService
import com.example.myapplication.utils.LocalDateTimeAdapter
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import java.time.LocalDateTime

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080/"
    @RequiresApi(Build.VERSION_CODES.O)
    fun getInstance(context: Context): AuthApiService {
        val gson = GsonBuilder()
            .registerTypeAdapter(
                LocalDateTime::class.java,
                LocalDateTimeAdapter()
            )
            .create()
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context.applicationContext)).build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApiService::class.java)


    }

}