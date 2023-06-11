package com.dicoding.batiksnapapplication.data.network

import android.content.Context
import androidx.viewbinding.BuildConfig
import com.dicoding.batiksnapapplication.data.AuthInterceptor
import com.dicoding.batiksnapapplication.data.Preference
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object{
        private fun getInterceptor(token: String?): OkHttpClient {
            val loggingInterceptor = if(BuildConfig.DEBUG) { HttpLoggingInterceptor().setLevel(
                HttpLoggingInterceptor.Level.BODY) }else { HttpLoggingInterceptor().setLevel(
                HttpLoggingInterceptor.Level.NONE) }
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

            return if (token.isNullOrEmpty()) {
                OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .build()
            } else {
                OkHttpClient.Builder()
                    .addInterceptor(AuthInterceptor(token))
                    .addInterceptor(loggingInterceptor)
                    .build()
            }
        }

        fun getApiService(context: Context): ApiService {

            val sharedPref = Preference.initPref(context, "onSignIn")
            val token = sharedPref.getString("token", null).toString()

            val retrofit = Retrofit.Builder()
                .baseUrl("http://34.128.79.103:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(getInterceptor(token))
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}