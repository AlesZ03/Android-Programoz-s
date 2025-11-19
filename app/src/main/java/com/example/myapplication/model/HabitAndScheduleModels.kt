package com.example.myapplication.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class ScheduleResponse(
    val id: Long,
    @SerializedName("start_time")
    val startTime: String? = null,
    @SerializedName("end_time")
    val endTime: String? = null,
    val status: String? = null,
    val date: String? = null,
    @SerializedName("is_custom")
    val isCustom: Boolean,
    @SerializedName("created_at")
    val createdAt: String? = null,
    @SerializedName("updated_at")
    val updatedAt: String? = null,
    val type: String? = null,
    @SerializedName("duration_minutes")
    val durationMinutes: Int? = null,
    val notes: String? = null,
    val participants: List<ParticipantDto>? = emptyList(),
    val habit: HabitResponse? = null,
    val progress: List<ProgressResponseDto>? = emptyList(),
    val isParticipantOnly: Boolean,
)

data class HabitResponse(
    val id: Long,
    val name: String,
    val description: String? = null,
    val category: HabitCategory,
    val goal: String,
    @SerializedName("created_at")
    val createdAt: String? = null,
    @SerializedName("updated_at")
    val updatedAt: String? = null
)

data class HabitCategory(
    val id: Long,
    val name: String,
    val iconUrl: String? = null
)

data class ProgressResponseDto(
    val id: Long,
    val userId: Long,
    val progressValue: Double,
    @SerializedName("updated_at")
    val updatedAt: String? = null
)

data class ParticipantDto(
    val id: Long,
    val name: String,
    val email: String,
    val profileImage: String? = null
)
data class AddHabitRequest(
    val name: String,
    val description: String,
    val categoryId: Long,
    val goal: String
)