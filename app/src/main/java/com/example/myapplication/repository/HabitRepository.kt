package com.example.myapplication.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.myapplication.model.AddHabitRequest
import com.example.myapplication.model.HabitResponse
import com.example.myapplication.network.RetrofitClient

@RequiresApi(Build.VERSION_CODES.O)
class HabitRepository(context: Context) {


    private val api by lazy { RetrofitClient.getInstance(context) }


    suspend fun addHabit(request: AddHabitRequest): HabitResponse {
        return api.addHabit(request)

    }
}
