package com.unware.mediaapp.ui.medialist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.unware.mediaapp.databinding.RowAudioVideoBinding
import com.unware.mediaapp.model.Audio

class MediaListAdapter(
    val listener: MediaListItemListener
) : ListAdapter<Audio, RecyclerView.ViewHolder>(MediaDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AudioViewHolder(
            RowAudioVideoBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mediaItem = getItem(position)
        (holder as AudioViewHolder).bind(mediaItem, listener)
    }

    interface MediaListItemListener {
        fun playMedia(audio: Audio, position: Int)
    }

    inner class AudioViewHolder(
        private val binding: RowAudioVideoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.setPlayMedia {
                binding.audio?.let { audio ->
                    playMedia(audio, it)
                }
            }
        }

        private fun playMedia(audio: Audio, view: View) {
            listener.playMedia(audio, adapterPosition)

            view.findNavController().navigate(
                MediaListFragmentDirections.actionOpenMediaDetail(audio)
            )
        }

        fun bind(item: Audio, listener: MediaListItemListener) {
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