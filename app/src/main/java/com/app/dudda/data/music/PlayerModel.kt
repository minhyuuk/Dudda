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

    fun updateCurrentPosition(musicModel: MusicModel) {
        currentPosition = playMusicList.indexOf(musicModel)
    }

    fun nextMusic(): MusicModel? {
        if(playMusicList.isEmpty()) return null

        currentPosition = if((currentPosition+1) == playMusicList.size) 0 else currentPosition + 1
        return playMusicList[currentPosition]
    }

    fun previousMusic(): MusicModel? {
        if(playMusicList.isEmpty()) return null

        currentPosition = if((currentPosition -1) < 0) playMusicList.lastIndex else currentPosition - 1
        return playMusicList[currentPosition]
    }
}

