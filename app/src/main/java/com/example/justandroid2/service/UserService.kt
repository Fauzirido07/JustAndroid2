package com.example.justandroid2.service

import com.example.justandroid2.respon.UserRespon
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface UserService {
    @GET("users")
    fun getData() : Call<List<UserRespon>>

    @DELETE("users/{id}")
    fun delete(@Path("id") id : Int) : Call<UserRespon>
}