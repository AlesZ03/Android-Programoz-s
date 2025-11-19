package com.example.myapplication.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.AddHabitRequest
import com.example.myapplication.repository.ScheduleRepository
import kotlinx.coroutines.launch

class AddHabitViewModel(
    private val repository: ScheduleRepository
) : ViewModel() {

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> get() = _success

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    @RequiresApi(Build.VERSION_CODES.O)
    fun addHabit(name: String, desc: String, categoryId: Long, goal: String) {
        viewModelScope.launch {
            try {
                val request = AddHabitRequest(
                    name = name,
                    description = desc,
                    categoryId = categoryId,
                    goal = goal
                )

                repository.addHabit(request)
                _success.postValue(true)

            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }
    }
}
