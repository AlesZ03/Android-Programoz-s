package com.example.myapplication.ui.home

import android.os.Build
import androidx.lifecycle.*
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.myapplication.repository.ScheduleRepository
import com.example.myapplication.model.ScheduleResponse
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class HomeViewModel(private val repository: ScheduleRepository) : ViewModel() {
    private val _schedules = MutableLiveData<List<ScheduleResponse>>()
    val schedules: LiveData<List<ScheduleResponse>> get() = _schedules

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private var _currentDay: String = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)

    init {
        Log.d("HomeViewModel", "ViewModel initialized, fetching schedule for today: $_currentDay")
        getScheduleByDay(_currentDay)
    }
    fun getScheduleByDay(day: String) {
        _currentDay = day // Store the day
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

    fun deleteSchedule(scheduleId: Long) {
        viewModelScope.launch {
            _isLoading.value = true // Show loading indicator during deletion
            try {
                repository.deleteSchedule(scheduleId)
                // Refresh the list for the current day
                getScheduleByDay(_currentDay)
                Log.d("HomeViewModel", "Successfully deleted schedule $scheduleId and refreshed list.")
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error deleting schedule", e)
                _errorMessage.value = e.message ?: "Failed to delete schedule"
                _isLoading.value = false // Hide loading indicator on error
            }
            // isLoading will be set to false in getScheduleByDay on success
        }
    }


    fun clearError() {
        _errorMessage.value = null
    }
}
