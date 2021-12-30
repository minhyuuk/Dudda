package com.app.dudda.data.music

import retrofit2.Call
import retrofit2.http.GET

interface MusicService {
    @GET("/v3/bf57b160-d50f-4946-be5d-d40be9090743")
    fun listMusics() : Call<MusicDTO>
}