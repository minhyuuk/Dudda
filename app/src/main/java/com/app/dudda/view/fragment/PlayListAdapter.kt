package com.app.dudda.view.fragment

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.dudda.R
import com.app.dudda.data.music.MusicModel
import com.bumptech.glide.Glide

class PlayListAdapter(private val callback: (MusicModel) -> Unit) :
    ListAdapter<MusicModel, PlayListAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: MusicModel) {

            val trackTextView = view.findViewById<TextView>(R.id.itemTrackTextView)
            val artistTextView = view.findViewById<TextView>(R.id.artistTextView)
            val coverImageView = view.findViewById<ImageView>(R.id.itemCoverImageView)

            trackTextView.text = item.track
            artistTextView.text = item.artistName

            Glide.with(coverImageView.context)
                .load(item.coverImageUrl)
                .into(coverImageView)

            if (item.isPlaying) {
                itemView.setBackgroundColor(Color.GRAY)
            } else {
                itemView.setBackgroundColor(Color.TRANSPARENT)
            }

            itemView.setOnClickListener { callback(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_music, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        currentList[position].also {
            holder.bind(item = it)
        }
    }

    companion object{
        val diffUtil = object: DiffUtil.ItemCallback<MusicModel>(){
            // id값 비교
            override fun areItemsTheSame(oldItem: MusicModel, newItem: MusicModel): Boolean {
                return oldItem.id == newItem.id
            }
            // contents값 비교
            override fun areContentsTheSame(oldItem: MusicModel, newItem: MusicModel): Boolean {
                return oldItem == newItem
            }

        }
    }
}