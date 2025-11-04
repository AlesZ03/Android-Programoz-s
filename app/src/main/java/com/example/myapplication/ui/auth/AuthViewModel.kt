package com.example.myapplication.ui.auth

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.myapplication.model.AuthResponse
import com.example.myapplication.repository.AuthRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AuthRepository(application)
    private val _authResult = MutableLiveData<Result<AuthResponse>>()
    val authResult: LiveData<Result<AuthResponse>> = _authResult

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = repository.login(email, password)
                handleResponse(response)
            } catch (e: Exception) {
                _authResult.postValue(Result.failure(e))
            }
        }
    }

    private fun handleResponse(response: Response<AuthResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _authResult.postValue(Result.success(response.body()!!))
        } else {
            val code = response.code()
            val errorBody = response.errorBody()?.string() ?: "No error body"
            Log.e("AuthViewModel", "Error $code: $errorBody")
            _authResult.postValue(Result.failure(Exception("Auth failed: $code - $errorBody")))
        }
    }
}