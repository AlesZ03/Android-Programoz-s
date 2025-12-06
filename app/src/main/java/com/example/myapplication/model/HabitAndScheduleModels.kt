package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

data class ScheduleResponse(

        val id: Long,

        @SerializedName("start_time")
        val start_time: String? = null,

        @SerializedName("end_time")
        val end_time: String? = null,

        val status: String? = null,
        val date: String? = null,

        @SerializedName("is_custom")
        val is_custom: Boolean,

        @SerializedName("created_at")
        val createdAt: String? = null,

        @SerializedName("updated_at")
        val updatedAt: String? = null,

        val type: String? = null,

        @SerializedName("duration_minutes")
        val duration_minutes: Int? = null,

        val notes: String? = null,
        val participantIds: List<ParticipantDto>? = emptyList(),
        val habit: HabitResponse? = null,
        val progress: List<ProgressResponseDto>? = emptyList(),

        // HOZZÁADVA: Ez az annotáció hiányzott
        @SerializedName("is_participant_only")
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

data class ScheduleRequest(
    val habitId: Long,
    val date: String,
    val start_time: String,
    val end_time: String,
    val duration_minutes: Int,
    val is_custom: Boolean,
    val participantIds: List<Long>,
    val notes: String
)
