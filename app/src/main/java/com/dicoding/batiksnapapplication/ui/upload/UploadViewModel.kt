package com.dicoding.batiksnapapplication.ui.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.batiksnapapplication.data.response.UploadResponse
import com.dicoding.batiksnapapplication.data.repositor.AuthRepositor
import com.dicoding.batiksnapapplication.data.repositor.Result
import okhttp3.MultipartBody

class UploadViewModel (private val authRepository: AuthRepositor) : ViewModel() {

    fun postImage(file: MultipartBody.Part): LiveData<Result<UploadResponse>> {
        return authRepository.postImage(file)
    }
}
