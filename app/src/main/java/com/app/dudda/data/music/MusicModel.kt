package com.app.dudda.data.music

data class MusicModel(
    val id: Long,
    val track: String,
    val streamUrl: String,
    val artistName: String,
    val coverImageUrl: String,
    val isPlaying: Boolean = false
)