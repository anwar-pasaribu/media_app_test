package com.unware.mediaapp.ui.medialist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.unware.mediaapp.databinding.RowAudioVideoBinding
import com.unware.mediaapp.model.Audio

class MediaListAdapter : ListAdapter<Audio, RecyclerView.ViewHolder>(MediaDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AudioViewHolder(
            RowAudioVideoBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mediaItem = getItem(position)
        (holder as AudioViewHolder).bind(mediaItem)
    }

    inner class AudioViewHolder(
        private val binding: RowAudioVideoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            // binding.
        }

        fun bind(item: Audio) {
            binding.apply {
                audio = item
                executePendingBindings()
            }
        }

    }
}

private class MediaDiffCallback : DiffUtil.ItemCallback<Audio>() {

    override fun areItemsTheSame(oldItem: Audio, newItem: Audio): Boolean {
        return oldItem.mediaId == newItem.mediaId
    }

    override fun areContentsTheSame(oldItem: Audio, newItem: Audio): Boolean {
        return oldItem == newItem
    }
}