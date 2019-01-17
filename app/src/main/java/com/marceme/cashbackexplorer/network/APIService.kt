package com.marceme.cashbackexplorer.network

import com.marceme.cashbackexplorer.model.User
import com.marceme.cashbackexplorer.model.UserResponse
import com.marceme.cashbackexplorer.model.VenuesResponse
import retrofit2.Call
import retrofit2.http.*


interface APIService {

    @POST("users")
    fun createUser(@Body user: User): Call<UserResponse>

    @POST("login")
    fun login(@Header("token") token:String): Call<UserResponse>

    @GET("venues")
    fun getVenues(@Header("token") token:String, @Query("city") city: String): Call<VenuesResponse>
}
