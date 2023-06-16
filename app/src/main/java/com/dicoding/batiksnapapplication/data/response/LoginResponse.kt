package com.dicoding.batiksnapapplication.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(


    @field:SerializedName("access_token")
    val access_token: String,


    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)

