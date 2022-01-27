package com.app.dudda.data.music

import retrofit2.Call
import retrofit2.http.GET

interface MusicService {
    // mocky에서 만든 임시 api, 음악을 불러오기 위해 사용
    @GET("/v3/bf57b160-d50f-4946-be5d-d40be9090743")
    fun listMusics() : Call<MusicDTO>
}