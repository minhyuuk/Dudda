package com.app.dudda.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.app.dudda.R
import com.app.dudda.data.music.MusicDTO
import com.app.dudda.data.music.MusicService
import com.app.dudda.data.music.mapper
import com.app.dudda.databinding.FragmentPlayerBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PlayerFragment : Fragment(R.layout.fragment_player){

    private var binding: FragmentPlayerBinding? = null
    private var boolOfWatchPlayerListView = true
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentPlayerBinding = FragmentPlayerBinding.bind(view)
        binding = fragmentPlayerBinding
        getMusicFromServer()
        initPlayListButton(fragmentPlayerBinding)
    }

    private fun getMusicFromServer() {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://run.mocky.io")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // MusicService에서 만든 interface call
        retrofit.create(MusicService::class.java)
            .also {
                it.listMusics()
                    .enqueue(object: Callback<MusicDTO>{
                        override fun onResponse(
                            call: Call<MusicDTO>,
                            response: Response<MusicDTO>
                        ) {
                           Log.d("PlayerFragment","${response.body()}")

                            // MusicDTO Nullable
                            response.body()?.let {
                                val modelList = it.musics.mapIndexed { index, musicEntity ->
                                    // mapper를 가지고 있음. MusicModelMapping 참고
                                    // 반환값은 MusicModel
                                    musicEntity.mapper(index.toLong())
                                }
                            }
                        }

                        override fun onFailure(call: Call<MusicDTO>, t: Throwable) {
                            Log.e("PlayerFragment","${t.message}")
                        }

                    })
            }


    }


    private fun initPlayListButton(fragmentPlayerBinding: FragmentPlayerBinding) {
        fragmentPlayerBinding.playlistImageView.setOnClickListener{
            // server에서 data가 불러와지지 않았을 경우
            // playlist click 시 예외처리.
            fragmentPlayerBinding.playListViewGroup.isVisible = boolOfWatchPlayerListView
            fragmentPlayerBinding.playerViewGroup.isVisible = boolOfWatchPlayerListView.not()

            boolOfWatchPlayerListView = !boolOfWatchPlayerListView
        }
    }

    companion object{
        // arguments에 값을 넣어주기 위해 만듦
        fun newInstance() : PlayerFragment {
            return PlayerFragment()
        }
    }
}