package com.dicoding.batiksnapapplication

import androidx.lifecycle.ViewModel
import com.dicoding.batiksnapapplication.data.repositor.AuthRepositor

class MainViewModel(private val pref: AuthRepositor) : ViewModel() {
    fun login(email: String, password: String) = pref.loginUser(email, password)
}