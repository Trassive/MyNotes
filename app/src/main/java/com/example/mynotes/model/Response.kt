package com.example.mynotes.model

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.StateFlow

typealias LogInResponse = Response<AuthResult>
typealias RegisterResponse = Response<Boolean>
typealias LogOutResponse = Response<Boolean>


sealed class Response<out T> {
    data object Loading : Response<Nothing>()
    data class Success<T>(val data: T?) : Response<T>()
    data class Error(val e: Exception) : Response<Nothing>()
}