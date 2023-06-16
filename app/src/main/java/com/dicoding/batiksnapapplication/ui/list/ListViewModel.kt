package com.dicoding.batiksnapapplication.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.batiksnapapplication.data.Batik
import com.dicoding.batiksnapapplication.data.repositor.AuthRepositor
import kotlinx.coroutines.launch

class ListViewModel(private val authRepositor: AuthRepositor): ViewModel() {
    var result_predict = " "
    private val _batik = MutableLiveData<List<Batik>>()
    var batik: LiveData<List<Batik>> = _batik

    fun getBatik() {
        viewModelScope.launch {
            _batik.postValue(authRepositor.getBatik(result_predict))
        }
    }

    fun getDetail(id: Int, pred: String) = authRepositor.getBatikDetail(id, pred)

}