package com.dicoding.batiksnapapplication.ui.detail

import androidx.lifecycle.ViewModel
import com.dicoding.batiksnapapplication.data.repositor.AuthRepositor

class DetailViewModel(private val authRepositor: AuthRepositor) : ViewModel() {
    var result_predict = " "
    fun getDetail(id: Int) = authRepositor.getBatikDetail(id, result_predict)
}