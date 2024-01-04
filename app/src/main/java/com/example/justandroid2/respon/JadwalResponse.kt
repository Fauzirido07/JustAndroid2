package com.example.justandroid2.respon

import com.google.gson.annotations.SerializedName

data class Jadwal<T>(
    @SerializedName("data")
    val data: T? = null
)
class JadwalResponse {
    @SerializedName("id")
    var id: Int = 0
    @SerializedName("attributes")
    var attributes:JadwalAttributes = JadwalAttributes()
}

class JadwalAttributes {
    var id_user: Jadwal<User>? = null
    var editor: Jadwal<User>? = null
    var date: String = ""
    var time: String = ""
    var link: String = ""
    var tawaran: String = ""
}