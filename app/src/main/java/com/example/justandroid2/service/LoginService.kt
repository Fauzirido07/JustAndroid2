package com.example.justandroid2.service

import com.example.justandroid2.data.LoginData
import com.example.justandroid2.respon.LoginRespon
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("auth/local")
    fun getData(@Body body: LoginData) : Call<LoginRespon>
}