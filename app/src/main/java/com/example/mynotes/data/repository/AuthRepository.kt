package com.example.mynotes.data.repository

import com.example.mynotes.model.LogInResponse
import com.example.mynotes.model.LogOutResponse
import com.example.mynotes.model.RegisterResponse
import com.example.mynotes.model.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth
){
    private val _authState = MutableStateFlow<FirebaseUser?>(null)
    val authState = _authState.asStateFlow()

    init {
        auth.addAuthStateListener { firebaseAuth ->
            _authState.value = firebaseAuth.currentUser
        }
    }
    suspend fun login(email: String, password: String): LogInResponse {
        return try{
            Response.Success(auth.signInWithEmailAndPassword(email, password).await())
        } catch(e: Exception){
            Response.Error(e)
        }
    }

    suspend fun register( email: String? = null, password: String?=null): RegisterResponse {
        return try {
            if(email != null && password != null){
                auth.createUserWithEmailAndPassword(email, password).await()
            } else{
                auth.signInAnonymously().await()
            }
            Response.Success(true)
        } catch (e: Exception) {
            Response.Error(e)
        }
    }


    fun logout(): LogOutResponse {
        return try {
            auth.signOut()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Error(e)
        }
    }
    fun updateUser(email: String, name: String, photoUrl: String){

    }

}