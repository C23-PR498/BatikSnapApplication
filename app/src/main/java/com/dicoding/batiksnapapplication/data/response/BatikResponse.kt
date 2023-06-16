package com.dicoding.batiksnapapplication.data.response

import com.dicoding.batiksnapapplication.data.Batik
import com.google.gson.annotations.SerializedName

data class BatikResponse (
    @field:SerializedName("error")
    val error: Boolean,
    @SerializedName("listBatik")
    val listBatik: List<Batik>,
    @field:SerializedName("message")
    val message: String
)
