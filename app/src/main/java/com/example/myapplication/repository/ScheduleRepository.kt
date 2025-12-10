package com.example.myapplication.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.myapplication.model.AddHabitRequest
import com.example.myapplication.model.HabitResponse
import com.example.myapplication.model.ScheduleRequest
import com.example.myapplication.model.ScheduleResponse
import com.example.myapplication.network.RetrofitClient

@RequiresApi(Build.VERSION_CODES.O)
public class ScheduleRepository(context: Context) {
    private val api by lazy { RetrofitClient.getInstance(context) }
    suspend fun getScheduleByDay(day: String): List<ScheduleResponse> {
        return api.getScheduleByDay(day)
    }
    suspend fun addSchedule(request: ScheduleRequest) {
        api.addSchedule(request)
    }

    suspend fun deleteSchedule(scheduleId: Long) {
        api.deleteSchedule(scheduleId)
    }
}
