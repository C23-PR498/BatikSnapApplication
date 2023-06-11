package com.dicoding.batiksnapapplication

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.batiksnapapplication.data.network.ApiConfig
import com.dicoding.batiksnapapplication.data.repositor.AuthRepositor

class ViewModelFactory (private val context: Context) : ViewModelProvider.Factory {

    private fun provideRepository(): AuthRepositor {
        val apiService = ApiConfig.getApiService(context)
        return AuthRepositor(apiService)
    }

//    private fun providesRepository(): StoryRepository {
//        val apiService = ApiConfig.getApiService(context)
//        return StoryRepository(apiService)
//    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {

            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(provideRepository()) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(provideRepository()) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}