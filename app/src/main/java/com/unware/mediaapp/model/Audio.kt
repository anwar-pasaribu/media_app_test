package com.unware.mediaapp.model

data class Audio(
    val mediaId: Long,
    val path: String,
    val name: String,
    val album: String,
    val albumId: Long,
    val albumArtPath: String,
    val artist: String
) {
    override fun toString(): String {
        return "Audio(name='$name', album='$album', albumId=$albumId, albumArtPath='$albumArtPath', artist='$artist', mediaPath='$path')"
    }
}