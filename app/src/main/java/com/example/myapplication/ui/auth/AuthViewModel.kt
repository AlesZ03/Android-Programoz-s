package com.example.myapplication.ui.auth

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.myapplication.model.AuthResponse

import com.example.myapplication.repository.AuthRepository
import com.example.myapplication.utils.Event
import kotlinx.coroutines.launch
import retrofit2.Response

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AuthRepository(application)

    // 🔹 2. MÓDOSÍTSD A LIVEDATA TÍPUSÁT
    private val _authResult = MutableLiveData<Event<Result<AuthResponse>>>()
    val authResult: LiveData<Event<Result<AuthResponse>>> = _authResult

    @RequiresApi(Build.VERSION_CODES.O)
    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = repository.login(email, password)
                if (response.isSuccessful && response.body() != null) {
                    // 🔹 3. CSOMAGOLD AZ EREDMÉNYT EVENT-BE
                    _authResult.value = Event(Result.success(response.body()!!))
                } else {
                    val error = Throwable(response.errorBody()?.string() ?: "Login failed")
                    _authResult.value = Event(Result.failure(error))
                }
            } catch (e: Exception) {
                _authResult.value = Event(Result.failure(e))
            }
        }
    }

    private fun handleResponse(response: Response<AuthResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _authResult.postValue(Event(Result.success(response.body()!!)))
        } else {
            val code = response.code()
            val errorBody = response.errorBody()?.string() ?: "No error body"
            Log.e("AuthViewModel", "Error $code: $errorBody")
            _authResult.postValue(Event(Result.failure(Exception("Auth failed: $code - $errorBody"))))
        }
    }
}