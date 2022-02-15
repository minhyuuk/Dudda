package com.app.dudda.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.dudda.R
import com.app.dudda.data.music.*
import com.app.dudda.databinding.FragmentPlayerBinding
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PlayerFragment : Fragment(R.layout.fragment_player) {

    private var model: PlayerModel = PlayerModel()
    private var binding: FragmentPlayerBinding? = null
    private var player: SimpleExoPlayer? = null
    private lateinit var playListAdapter: PlayListAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentPlayerBinding = FragmentPlayerBinding.bind(view)
        binding = fragmentPlayerBinding

        initPlayView(fragmentPlayerBinding)
        initPlayListButton(fragmentPlayerBinding)
        initPlayControlButton(fragmentPlayerBinding)
        initRecyclerView(fragmentPlayerBinding)

        getMusicFromServer()
    }

    private fun initPlayControlButton(fragmentPlayerBinding: FragmentPlayerBinding) {
        fragmentPlayerBinding.playControlImageView.setOnClickListener {
            val player = this.player ?: return@setOnClickListener

            if (player.isPlaying) {
                // 음악 중지
                player.pause()
            } else {
                // 음악 재생
                player.play()
            }
        }
        fragmentPlayerBinding.skipNextImageView.setOnClickListener {
            val nextMusic = model.nextMusic() ?: return@setOnClickListener
            playMusic(nextMusic)
        }
        fragmentPlayerBinding.skipPreviousImageView.setOnClickListener {
            val previousMusic = model.previousMusic() ?: return@setOnClickListener
            playMusic(previousMusic)
        }
    }

    private fun initPlayView(fragmentPlayerBinding: FragmentPlayerBinding) {
        // exoPlayer -> simpleExoPlayer
        context?.let {
            player = SimpleExoPlayer.Builder(it).build()
        }

        fragmentPlayerBinding.playerView.player = player

        binding?.let { binding ->
            player?.addListener(object : Player.EventListener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)

                    if (isPlaying) {
                        binding.playControlImageView.setImageResource(R.drawable.ic_baseline_pause_48)
                    } else {
                        binding.playControlImageView.setImageResource(R.drawable.ic_baseline_play_arrow_48)
                    }
                }

                override fun onPlaybackStateChanged(state: Int) {
                    super.onPlaybackStateChanged(state)
                    // getPlaybackState를 이용해 state 값 리턴
                }

                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    super.onMediaItemTransition(mediaItem, reason)
                    // recyclerview adapter 갱신
                    // currentposition 초기화
                    val newIndex = mediaItem?.mediaId ?: return
                    model.currentPosition = newIndex.toInt()
                    updatePlayerView(model.currentMusicModel())

                    playListAdapter.submitList(model.getAdapterModels())
                }
            })
        }
    }

    private fun updatePlayerView(currentMusicModel: MusicModel?) {
        currentMusicModel ?: return

        binding?.let { binding ->
            binding.trackTextView.text = currentMusicModel.track
            binding.artistTextView.text = currentMusicModel.artistName
            Glide.with(binding.coverImageView.context)
                .load(currentMusicModel.coverImageUrl)
                .into(binding.coverImageView)
        }
    }

    private fun initRecyclerView(fragmentPlayerBinding: FragmentPlayerBinding) {
        playListAdapter = PlayListAdapter {
            // 음악 재생하는 함수
            playMusic(it)
        }
        fragmentPlayerBinding.playListRecyclerView.apply {
            adapter = playListAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun getMusicFromServer() {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://run.mocky.io")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // MusicService에서 만든 인터페이스 호출
        retrofit.create(MusicService::class.java)
            .also {
                it.listMusics()
                    .enqueue(object : Callback<MusicDTO> {
                        override fun onResponse(
                            call: Call<MusicDTO>,
                            response: Response<MusicDTO>
                        ) {
                            Log.d("PlayerFragment", "${response.body()}")

                            // MusicDTO Nullable
                            response.body()?.let {

                                model = it.mapper()

                                setMusicList(model.getAdapterModels())
                                playListAdapter.submitList(model.getAdapterModels())
                            }
                        }

                        override fun onFailure(call: Call<MusicDTO>, t: Throwable) {
                            Log.e("PlayerFragment", "${t.message}")
                        }

                    })
            }
    }

    private fun setMusicList(modelList: List<MusicModel>) {
        context?.let {
            player?.addMediaItems(modelList.map { musicModel ->
                MediaItem.Builder()
                    .setMediaId(musicModel.id.toString())
                    .setUri(musicModel.streamUrl)
                    .build()
            })
            player?.prepare()
        }
    }

    private fun playMusic(musicModel: MusicModel){
        model.updateCurrentPosition(musicModel)
        player?.seekTo(model.currentPosition, 0)
        player?.play()
    }


    private fun initPlayListButton(fragmentPlayerBinding: FragmentPlayerBinding) {
        fragmentPlayerBinding.playlistImageView.setOnClickListener {
            // server에서 data가 불러와지지 않았을 경우
            // playlist click 시 예외처리.
            if(model.currentPosition == -1) return@setOnClickListener

            fragmentPlayerBinding.playListViewGroup.isVisible = model.isWatchingPlayListView
            fragmentPlayerBinding.playerViewGroup.isVisible = model.isWatchingPlayListView.not()

            model.isWatchingPlayListView = !model.isWatchingPlayListView
        }
    }

    companion object {
        // arguments에 값을 넣어주기 위해 만듦
        fun newInstance(): PlayerFragment {
            return PlayerFragment()
        }
    }
}