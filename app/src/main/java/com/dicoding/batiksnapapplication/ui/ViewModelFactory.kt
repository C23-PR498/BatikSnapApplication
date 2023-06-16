package com.dicoding.batiksnapapplication.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.batiksnapapplication.MainViewModel
import com.dicoding.batiksnapapplication.RegisterViewModel
import com.dicoding.batiksnapapplication.data.network.ApiConfig
import com.dicoding.batiksnapapplication.data.repositor.AuthRepositor
import com.dicoding.batiksnapapplication.ui.detail.DetailViewModel
import com.dicoding.batiksnapapplication.ui.list.ListViewModel
import com.dicoding.batiksnapapplication.ui.upload.UploadViewModel

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    private fun provideRepository(): AuthRepositor {
        val apiService = ApiConfig.getApiService(context)
        return AuthRepositor(apiService)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {

            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(provideRepository()) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(provideRepository()) as T
            }
            modelClass.isAssignableFrom(ListViewModel::class.java) -> {
                ListViewModel(provideRepository()) as T
            }
            modelClass.isAssignableFrom(UploadViewModel::class.java) -> {
                UploadViewModel(provideRepository()) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(provideRepository()) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}