package com.example.myapplication.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.ProfileResponse
import com.example.myapplication.repository.AuthRepository
import com.example.myapplication.utils.Event
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = AuthRepository(application)

    private val _profile = MutableLiveData<ProfileResponse>()
    val profile: LiveData<ProfileResponse> = _profile

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _navigateToLogin = MutableLiveData<Event<Unit>>()
    val navigateToLogin: LiveData<Event<Unit>> = _navigateToLogin

    init {
        fetchProfile()
    }

    fun fetchProfile() {
        viewModelScope.launch {
            val result = authRepository.getProfile()
            result.onSuccess {
                _profile.value = it
            }.onFailure {
                _error.value = "Hiba a profiladatok lekérése közben: ${it.message}"
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _navigateToLogin.value = Event(Unit) // Esemény küldése a navigációhoz
        }
    }
}
