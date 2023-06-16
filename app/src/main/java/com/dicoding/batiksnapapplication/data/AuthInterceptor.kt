package com.dicoding.batiksnapapplication.data

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthInterceptor(private var token: String): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()

        val request = if (originalRequest.header("No-Authentication") == null && token.isNotEmpty()) {
            val finalToken = "Bearer $token"
            Log.d("Interceptor", "Token Value: $finalToken")  // Use Log.d instead of println
            originalRequest.newBuilder()
                .addHeader("Authorization", finalToken)
                .build()
        } else {
            originalRequest
        }

        return chain.proceed(request)
    }
}
