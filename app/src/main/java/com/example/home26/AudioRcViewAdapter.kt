package com.example.home26

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.home26.databinding.AudioListItemBinding

class AudioRcViewAdapter() : RecyclerView.Adapter<AudioRcViewAdapter.AudioViewHolder>() {

    var audio: List<AudioEnity> = emptyList()
    set(value) {
        field = value
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AudioListItemBinding.inflate(inflater, parent, false)
        binding.root
        return AudioViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
        val item = audio[position]

        with(holder.binding) {
            holder.itemView.tag = item

            Glide.with(imageView.context)
                .load(item.image)
                .circleCrop()
                .into(imageView)

            artistName.text = item.artistName
            audioName.text = item.audioName
        }

    }

    override fun getItemCount() : Int{
        return audio.size
    }

    class AudioViewHolder(
        val binding: AudioListItemBinding
    ) : RecyclerView.ViewHolder(binding.root)
}


