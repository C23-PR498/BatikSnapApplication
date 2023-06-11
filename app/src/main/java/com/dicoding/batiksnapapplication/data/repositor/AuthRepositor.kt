package com.dicoding.batiksnapapplication.data.repositor

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.batiksnapapplication.data.LoginResponse
import com.dicoding.batiksnapapplication.data.RegisterResponse
import com.dicoding.batiksnapapplication.data.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.dicoding.batiksnapapplication.data.repositor.Result


class AuthRepositor(private val apinetwork: ApiService) {
    fun register(name: String, email: String, password: String): LiveData<Result<RegisterResponse>> = liveData(
        Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = apinetwork.register(name, email, password).execute()
            if (response.isSuccessful) {
                emit(Result.Success(response.body()!!))
            } else {
                emit(Result.Error(response.errorBody()?.string() ?: "Unknown error"))
            }
        } catch (e: Exception) {
            Log.e("AuthRepositor", "register: ${e.message}", e)
            emit(Result.Error(e.message ?: "Unknown error"))
        }
    }

    fun loginUser(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = withContext(Dispatchers.IO) {
                apinetwork.login(email, password).execute()
            }
            if (response.isSuccessful) {
                emit(Result.Success(response.body()!!))
            } else {
                emit(Result.Error(response.errorBody()?.string() ?: "Unknown error"))
            }
        } catch (e: Exception) {
            Log.e("AuthRepositor", "login: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }



}