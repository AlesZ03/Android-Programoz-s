package com.example.myapplication.repository

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.myapplication.model.AuthRequest
import com.example.myapplication.model.AuthResponse
import com.example.myapplication.model.ProfileResponse
import com.example.myapplication.network.RetrofitClient
import com.example.myapplication.utils.SessionManager
import retrofit2.Response

class AuthRepository(private val context: Context) {

    @RequiresApi(Build.VERSION_CODES.O)
    private val api = RetrofitClient.getInstance(context)
    // 🔹 JAVÍTÁS: Győződj meg róla, hogy itt 'applicationContext'-et használsz!
    private val sessionManager = SessionManager(context.applicationContext)

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun login(email: String, password: String): Response<AuthResponse> {
        val response = api.login(AuthRequest(email, password))


        if (response.isSuccessful) {
            response.body()?.let { authResponse ->
                val tokenToSave = authResponse.tokens.accessToken
                Log.d("AuthRepository", "Sikeres login, mentésre kerülő token: $tokenToSave")
                sessionManager.saveAuthToken(tokenToSave)
            }
        }
        return response
    }
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getProfile(): Result<ProfileResponse> {
        return try {
            val response = api.getProfile()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Throwable(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        sessionManager.clearAuthToken()
    }
}
