package com.playbackd.model

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val password: String,
    val role: String
)

data class UserLogin(
    val email: String,
    val password: String,
)

data class LoginResponse(
    val msg: String
)

data class UserRegister(
    val name: String,
    val email: String,
    val password: String
)

data class RegisterResponse(
    val msg: User
)
