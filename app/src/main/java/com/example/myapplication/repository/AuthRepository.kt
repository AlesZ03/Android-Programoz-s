package com.example.myapplication.repository

import android.content.Context
import com.example.myapplication.model.AuthRequest
import com.example.myapplication.network.RetrofitClient

class AuthRepository(context: Context) {
    private val api = RetrofitClient.getInstance(context)
    suspend fun login(email: String, password: String) =
        api.login(AuthRequest(email = email, password = password))
}