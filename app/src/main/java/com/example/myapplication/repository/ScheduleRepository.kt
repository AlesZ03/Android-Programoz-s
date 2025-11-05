package com.example.myapplication.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.myapplication.model.ScheduleResponse
import com.example.myapplication.network.RetrofitClient

@RequiresApi(Build.VERSION_CODES.O)
public class ScheduleRepository(context: Context) {
    private val api by lazy { RetrofitClient.getInstance(context) }
    suspend fun getScheduleByDay(day: String): List<ScheduleResponse> {
        return api.getScheduleByDay(day)
    }
}
