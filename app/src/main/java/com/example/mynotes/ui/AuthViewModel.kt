package com.example.mynotes.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotes.data.repository.AuthRepository
import com.example.mynotes.model.LogInResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository):ViewModel() {
    val authState = authRepository.authState
    val email = MutableStateFlow("")
    val password = MutableStateFlow("")

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError = _emailError.asStateFlow()

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError = _passwordError.asStateFlow()

    fun login() {
        viewModelScope.launch {
            try {
                if (!isValidEmail(email.value)) {
                    _emailError.value = "Invalid email format"
                    return@launch
                }
                if (!isValidPassword(password.value)) {
                    _passwordError.value = "Password must be at least 6 characters"
                    return@launch
                }
                val response = authRepository.login(email.value, password.value)
            } catch (e: Exception) {
                _emailError.value = e.message ?: "Unknown error"
            }
        }
    }

    fun register() {
        viewModelScope.launch {
            try {
                if (!isValidEmail(email.value)) {
                    _emailError.value = "Invalid email format"
                    return@launch
                }
                if (!isValidPassword(password.value)) {
                    _passwordError.value = "Password must be at least 6 characters"
                    return@launch
                }
                val response = authRepository.register( email.value, password.value)
            } catch (e: Exception) {
                _emailError.value = e.message ?: "Unknown error"
            }
        }
    }

    fun logout() = authRepository.logout()

    private companion object{
        private fun isValidEmail(email: String): Boolean {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        private fun isValidPassword(password: String): Boolean {
            return password.length >= 6
        }
    }
}
