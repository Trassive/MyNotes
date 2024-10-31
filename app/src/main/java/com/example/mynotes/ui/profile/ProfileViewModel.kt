package com.example.mynotes.ui.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotes.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(private val authRepository: AuthRepository): ViewModel() {
    private val user = authRepository.authState.value
    val email = MutableStateFlow(user?.email?: "")
    val name = MutableStateFlow(user?.displayName?: "Anyonymous")
    val photoUrl = MutableStateFlow(user?.photoUrl.toString())

    private val _isEmailVerified = MutableStateFlow(user?.isEmailVerified?: false)
    val isEmailVerified = _isEmailVerified.asStateFlow()

    private val _editingEnabled = MutableStateFlow(false)
    val editingEnabled = _editingEnabled.asStateFlow()

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError = _emailError.asStateFlow()

    fun updateProfile(imageUri: Uri? = null) {
        fun isValidEmail(): Boolean {
            return email.value.let { android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches() } ?: false
        }
        if (!isValidEmail()) {
            _emailError.value = "Invalid email format"
            return
        }
        viewModelScope.launch{
            try{
                authRepository.updateUser(email.value, name.value, "")
            } catch (e: Exception){
                _emailError.value = e.message ?: "Unknown error"
            }
        }
    }
    fun toggleEditing() {
        _editingEnabled.update {
            !_editingEnabled.value
        }
    }
    fun reset(){
        email.value = user?.email ?: ""
        name.value = user?.displayName ?: "Anonymous"
        photoUrl.value = user?.photoUrl.toString()
        _isEmailVerified.value = user?.isEmailVerified ?: false
        _editingEnabled.value = false
        _emailError.value = null
    }

}