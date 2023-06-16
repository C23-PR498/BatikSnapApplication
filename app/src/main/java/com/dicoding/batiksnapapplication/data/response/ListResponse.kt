package com.dicoding.batiksnapapplication.data.response

import com.dicoding.batiksnapapplication.data.Batik
import com.google.gson.annotations.SerializedName

data class ListResponse (
    @SerializedName("error")
    val error: Boolean,

    @SerializedName("listBatik")
    val listBatik: List<Batik>,

    @SerializedName("message")
    val message: String
)