package com.example.myapplication

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface MyApi {
    @Multipart
    @POST("/webapp/Storage/upload.jsp")
    fun sendImage(
        @Part imageFile: MultipartBody.Part
    ): Call<String>
}