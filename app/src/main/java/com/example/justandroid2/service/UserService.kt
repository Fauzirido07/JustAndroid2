package com.example.justandroid2.service

import com.example.justandroid2.data.UpdateData
import com.example.justandroid2.respon.LoginRespon
import com.example.justandroid2.respon.UserRespon
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {
    @GET("users")
    fun getData(@Query("filters[job]") job: String?) : Call<List<UserRespon>>


    @GET("users")
    fun getDataEditor(@Query("filters[id]") id: Int?) : Call<List<UserRespon>>


    @DELETE("users/{id}")
    fun delete(@Path("id") id : Int) : Call<UserRespon>

    @PUT("users/{id}")
    fun save(@Path("id") id: String?, @Body body: UpdateData): Call<LoginRespon>
}