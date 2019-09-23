package com.unware.mediaapp.ui.mediadetail

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.unware.mediaapp.R
import com.unware.mediaapp.model.Audio
import kotlinx.android.synthetic.main.media_detail_fragment.*


class MediaDetailFragment : Fragment() {

    private var playWhenReady: Boolean = false
    private var currentWindow: Int = 0
    private var playbackPosition: Long = 0
    private val args: MediaDetailFragmentArgs by navArgs()

    private lateinit var mediaItem: Audio

    private var playerFactory: SimpleExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mediaItem = args.mediaItem
        println("Media item: $mediaItem")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.media_detail_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            initPlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M
            && playerFactory == null) {
            initPlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            releasePlayer()
        }
    }

    private fun initPlayer() {
        playerFactory = ExoPlayerFactory.newSimpleInstance(
            DefaultRenderersFactory(requireContext()),
            DefaultTrackSelector(), DefaultLoadControl()
        )

        video_view.player = playerFactory

        playerFactory?.playWhenReady = true
        playerFactory?.seekTo(currentWindow, playbackPosition)

        val uri = Uri.parse(mediaItem.path)
        val mediaSource = ExtractorMediaSource.Factory(
            DefaultDataSourceFactory(requireContext(), "exoplayer-test")
        ).createMediaSource(uri)
        playerFactory?.prepare(mediaSource, false, false)
    }

    private fun releasePlayer() {
        playerFactory?.let {
            playbackPosition = it.currentPosition
            currentWindow = it.currentWindowIndex
            playWhenReady = it.playWhenReady
            it.release()
            playerFactory = null
        }
    }

}
