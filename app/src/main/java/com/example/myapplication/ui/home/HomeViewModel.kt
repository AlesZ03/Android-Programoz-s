package com.example.myapplication.ui.home

import android.os.Build
import androidx.lifecycle.*
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.myapplication.repository.ScheduleRepository
import com.example.myapplication.model.ScheduleResponse
import kotlinx.coroutines.launch
/**
 * ViewModel that manages schedule-related data for the ScheduleFragment.
 */

class HomeViewModel(private val repository: ScheduleRepository) :
    ViewModel() {
    private val _schedules = MutableLiveData<List<ScheduleResponse>>()
    val schedules: LiveData<List<ScheduleResponse>> get() = _schedules
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage
    @RequiresApi(Build.VERSION_CODES.O)
    fun getScheduleByDay(day: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getScheduleByDay(day)
                Log.d("HomeViewModel", "Fetched schedules for $day: $response")
                _schedules.value = response
                _errorMessage.value = null
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error fetching schedules", e)
                _errorMessage.value = e.message ?: "Failed to load schedules"
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun clearError() {
        _errorMessage.value = null
    }
}