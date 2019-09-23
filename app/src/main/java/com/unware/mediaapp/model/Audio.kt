package com.unware.mediaapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Audio(
    val mediaId: Long,
    val path: String,
    val name: String,
    val album: String,
    val albumId: Long,
    val albumArtPath: String,
    val artist: String
) : Parcelable {

    var mediaType: Int = AUDIO

    companion object {
        const val AUDIO = 0
        const val VIDEO = 1
    }

    override fun toString(): String {
        return "Audio(name='$name', album='$album', albumId=$albumId, albumArtPath='$albumArtPath', artist='$artist', mediaPath='$path')"
    }
}