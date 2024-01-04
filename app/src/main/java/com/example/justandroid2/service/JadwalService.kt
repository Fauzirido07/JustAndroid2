package com.example.justandroid2.service

import com.example.justandroid2.data.jadwalDataWrapper
import com.example.justandroid2.respon.Jadwal
import com.example.justandroid2.respon.JadwalResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface JadwalService {
    @POST("jadwals")
    fun buatJadwal(@Body jadwalData: jadwalDataWrapper) : Call<JadwalResponse>

    @PUT("jadwals/{id}")
    fun updateTawaran(@Path("id") id: String?, @Body jadwalData: jadwalDataWrapper): Call<JadwalResponse>

    @GET("jadwals")
    fun getAllTawaran(@Query("filters[editor]") editor: String?, @Query("populate") populate: String? ): Call<Jadwal<List<JadwalResponse>>>
}