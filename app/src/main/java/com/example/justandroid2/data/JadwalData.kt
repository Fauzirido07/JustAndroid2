package com.example.justandroid2.data

import com.google.gson.annotations.SerializedName

data class jadwalDataWrapper(@SerializedName("data") val jadwalData: jadwalData)

data class jadwalData(
    val id_user: Int,
    val editor: Int,
    val date: String,
    val time: String,
    val link: String,
    val tawaran: String
)
