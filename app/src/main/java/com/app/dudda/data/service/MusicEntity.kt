package com.app.dudda.data.service

import com.google.gson.annotations.SerializedName

data class MusicEntity(
    @SerializedName("track") val track: String,
    @SerializedName("streamUrl") val streamUrl: String,
    @SerializedName("artist") val artistName: String,
    @SerializedName("coverImageUrl") val coverImageUrl: String
)