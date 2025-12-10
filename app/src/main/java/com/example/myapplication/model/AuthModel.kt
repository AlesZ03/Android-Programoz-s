package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

data class User(
    val id: String,
    val email: String,
    val name: String
)
data class AuthRequest(
    val email: String,
    val password: String,
    val name: String? = null // only used in signup
)
data class Tokens(
    val accessToken: String,
    val refreshToken: String
)
data class AuthResponse(
    val tokens: Tokens,
    val user: User
)

data class ProfileResponse(
    val id: Int,
    val email: String,
    val username: String,
    val description: String?,
    @SerializedName("profileImageBase64") // A Base64 stringet tartalmazó mező
    val profileImageBase64: String?
)