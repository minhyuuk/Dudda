package com.app.dudda.data.music

fun MusicEntity.mapper(id: Long): MusicModel =
    MusicModel(
        id = id,
        streamUrl = this.streamUrl,
        coverImageUrl = this.coverImageUrl,
        track = this.track,
        artistName = artistName
    )

fun MusicDTO.mapper(): PlayerModel =
    PlayerModel(
        playMusicList = this.musics.mapIndexed { index, musicEntity ->
            // mapper를 가지고 있음. MusicModelMapping 참고
            // 반환값은 MusicModel
            musicEntity.mapper(index.toLong())
        }
    )


