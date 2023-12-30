package com.example.justandroid2.service

import com.example.justandroid2.data.jadwalDataWrapper
import com.example.justandroid2.respon.JadwalResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface JadwalService {
    @POST("jadwals")
    fun buatJadwal(@Body jadwalData: jadwalDataWrapper) : Call<JadwalResponse>
}