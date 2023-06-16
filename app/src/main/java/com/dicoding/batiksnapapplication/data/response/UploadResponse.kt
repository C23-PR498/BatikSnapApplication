package com.dicoding.batiksnapapplication.data.response

import com.dicoding.batiksnapapplication.data.Batik
import com.google.gson.annotations.SerializedName

data class UploadResponse (
    @field:SerializedName("access_token")
    val access_token: String,

    @field:SerializedName("error")
    val error: Boolean,

    @SerializedName("listBatik")
    val listBatik: List<Batik>,

    @field:SerializedName("predict")
    val predict: String,

    @field:SerializedName("result_predict")
    val resultPredict: String = predict
)