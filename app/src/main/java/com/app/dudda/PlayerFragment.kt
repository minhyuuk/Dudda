package com.app.dudda

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

class PlayerFragment : Fragment(R.layout.fragment_player){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    
    companion object{
        // arguments에 값을 넣어주기 위해 만듦
        fun newInstance() : PlayerFragment{
            return PlayerFragment()
        }
    }
}