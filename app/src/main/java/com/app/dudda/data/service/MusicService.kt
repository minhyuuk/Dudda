package com.app.dudda.data.service

import retrofit2.Call
import retrofit2.http.GET

interface MusicService {
    @GET("/v3/32878bf5-e124-4cc7-95de-7d26c86ab1e9")
    fun listMusics() : Call<MusicDTO>
}