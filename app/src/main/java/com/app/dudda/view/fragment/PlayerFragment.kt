package com.app.dudda.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.dudda.R
import com.app.dudda.data.music.MusicDTO
import com.app.dudda.data.music.MusicService
import com.app.dudda.data.music.mapper
import com.app.dudda.databinding.FragmentPlayerBinding
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PlayerFragment : Fragment(R.layout.fragment_player) {

    private var binding: FragmentPlayerBinding? = null
    private var boolOfWatchPlayerListView = true
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

        }
        fragmentPlayerBinding.skipPreviousImageView.setOnClickListener {

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
                        binding.playControlImageView.setImageResource(R.drawable.ic_baseline_playlist_play_48)
                    }
                }
            })
        }
    }

    private fun initRecyclerView(fragmentPlayerBinding: FragmentPlayerBinding) {
        playListAdapter = PlayListAdapter {
            // 음악 재생하는 함수
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
                                val modelList = it.musics.mapIndexed { index, musicEntity ->
                                    // mapper를 가지고 있음. MusicModelMapping 참고
                                    // 반환값은 MusicModel
                                    musicEntity.mapper(index.toLong())
                                }
                                playListAdapter.submitList(modelList)
                            }
                        }

                        override fun onFailure(call: Call<MusicDTO>, t: Throwable) {
                            Log.e("PlayerFragment", "${t.message}")
                        }

                    })
            }
    }


    private fun initPlayListButton(fragmentPlayerBinding: FragmentPlayerBinding) {
        fragmentPlayerBinding.playlistImageView.setOnClickListener {
            // server에서 data가 불러와지지 않았을 경우
            // playlist click 시 예외처리.
            fragmentPlayerBinding.playListViewGroup.isVisible = boolOfWatchPlayerListView
            fragmentPlayerBinding.playerViewGroup.isVisible = boolOfWatchPlayerListView.not()

            boolOfWatchPlayerListView = !boolOfWatchPlayerListView
        }
    }

    companion object {
        // arguments에 값을 넣어주기 위해 만듦
        fun newInstance(): PlayerFragment {
            return PlayerFragment()
        }
    }
}