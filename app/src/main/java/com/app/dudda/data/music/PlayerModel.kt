package com.app.dudda.data.music

data class PlayerModel (
    private val playMusicList: List<MusicModel> = emptyList(),
    var currentPosition: Int = -1,
    var isWatchingPlayListView: Boolean = true
){
    // 음악 재생여부 확인
    fun getAdapterModels(): List<MusicModel>{
        return playMusicList.mapIndexed{index,musicModel ->
            val newItem = musicModel.copy(
                isPlaying = index == currentPosition
            )
            newItem
        }
    }
}

