package com.dicoding.batiksnapapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "batik")
data class Batik(
    @field:SerializedName("createdAt")
    val createdAt: String,
    @field:SerializedName("deskripsi")
    val deskripsi: String,
    @PrimaryKey
    @field:SerializedName("id")
    val id: Int,
    @field:SerializedName("nama")
    val nama: String,
    @field:SerializedName("asal")
    val asal: String,
    @field:SerializedName("sejarah")
    val sejarah: String,
    @field:SerializedName("image")
    val image: String
)