package com.example.myapplication.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.ScheduleRequest
import com.example.myapplication.repository.ScheduleRepository
import kotlinx.coroutines.launch

class ScheduleViewModel(
    private val repository: ScheduleRepository
) : ViewModel() {

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> get() = _success

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    @RequiresApi(Build.VERSION_CODES.O)
    fun addSchedule(
        habitId: Long,
        date: String,
        startTime: String,
        endTime: String,
        durationMinutes: Int,
        participantIds: String,
        notes: String
    ) {
        viewModelScope.launch {
            try {
                val startDateTimeIso = "${date}T${startTime}.622Z"
                val endDateTimeIso = "${date}T${endTime}.622Z"

                val schedule = ScheduleRequest(
                    habitId = habitId,
                    date = startDateTimeIso,
                    start_time = startDateTimeIso,
                    end_time = endDateTimeIso,
                    duration_minutes = durationMinutes,
                    is_custom = true,
                    participantIds = if (participantIds.isNotBlank()) {
                        participantIds.split(",").mapNotNull { it.trim().toLongOrNull() }
                    } else {
                        emptyList()
                    },
                    notes = notes
                )

                repository.addSchedule(schedule)
                _success.postValue(true)

            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }
    }
}
