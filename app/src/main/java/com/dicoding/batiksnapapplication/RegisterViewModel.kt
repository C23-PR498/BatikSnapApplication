package com.dicoding.batiksnapapplication

import androidx.lifecycle.ViewModel
import com.dicoding.batiksnapapplication.data.repositor.AuthRepositor

class RegisterViewModel(private val pref: AuthRepositor) : ViewModel() {
    fun saveUser(name: String, email: String, password: String) = pref.register(name, email, password)
}