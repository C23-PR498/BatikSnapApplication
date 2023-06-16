package com.dicoding.batiksnapapplication.data.network

import com.dicoding.batiksnapapplication.data.*
import com.dicoding.batiksnapapplication.data.response.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("login")
     fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @GET("batik")
    fun batik(
        @Field("id") id: Int,
        @Field("nama") nama: String,
        @Field("deskripsi") deskripsi: String,
        @Field("asal") asal: String,
        @Field("sejarah") sejarah: String,
        @Field("image") image: String
    ): Call<BatikResponse>

    @Multipart
    @POST("upload")
    suspend fun postImage(
        @Part file: MultipartBody.Part
    ): UploadResponse


    @GET("list/{result_predict}")
    suspend fun getBatik(
        @Path("result_predict") resultPredict: String
    ) : ListResponse


    @GET("list/{result_predict}/{id}")
    fun detail(
        @Path("result_predict") resultPredict: String,
        @Path("id") id: Int,
    ) : Call<List<Batik>>

}