package com.app.dudda.data.music

fun MusicEntity.mapper(id: Long) : MusicModel =
    MusicModel(
        id = id,
        streamUrl = this.streamUrl,
        coverImageUrl = this.coverImageUrl,
        track = this.track,
        artistName = artistName
    )

