package com.app.dudda.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.app.dudda.view.fragment.PlayerFragment
import com.app.dudda.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, PlayerFragment.newInstance())
            .commit()

    }
}