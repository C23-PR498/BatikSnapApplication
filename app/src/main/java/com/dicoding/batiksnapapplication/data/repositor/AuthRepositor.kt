package com.dicoding.batiksnapapplication.data.repositor

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.batiksnapapplication.data.Batik

import com.dicoding.batiksnapapplication.data.network.ApiService
import com.dicoding.batiksnapapplication.data.response.*
//import com.dicoding.batiksnapapplication.data.network.BatikPagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody


class AuthRepositor(private val apiNetwork: ApiService) {
    fun register(
        name: String,
        email: String,
        password: String
    ): LiveData<Result<RegisterResponse>> = liveData(
        Dispatchers.IO
    ) {
        emit(Result.Loading)
        try {
            val response = apiNetwork.register(name, email, password).execute()
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
                apiNetwork.login(email, password).execute()
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

    fun postImage(file: MultipartBody.Part): LiveData<Result<UploadResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiNetwork.postImage(file)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.e("UploadViewModel", "postImage: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun getBatik(resultPredict: String): List<Batik> {
        val response = apiNetwork.getBatik(resultPredict)
        return response.listBatik
    }

    fun getBatikDetail(id: Int, resultPredict: String): LiveData<Result<Batik>> = liveData {
        emit(Result.Loading)
        try {
            Log.e("AuthRepositor", "getbatik: ${id} | $resultPredict")
            val response = withContext(Dispatchers.IO) {
                apiNetwork.detail(resultPredict, id).execute()
            }
            if (response.isSuccessful) {
                emit(Result.Success(response.body()!!.first()))
            } else {
                emit(Result.Error(response.errorBody()?.string() ?: "Unknown error"))
            }
        } catch (e: Exception) {
            Log.e("AuthRepositor", "getbatik: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

}