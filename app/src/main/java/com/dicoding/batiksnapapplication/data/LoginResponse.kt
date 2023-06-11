package com.dicoding.batiksnapapplication.data

import com.google.gson.annotations.SerializedName

data class LoginResponse(

//    @field:SerializedName("loginResult")
//    val loginResult: LoginResult,

    @field:SerializedName("access_token")
    val access_token: String,


    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)

//data class LoginResult(
//
//    @field:SerializedName("message")
//    val message: String,
//
//
//    @field:SerializedName("access_token")
//    val access_token: String
//)
