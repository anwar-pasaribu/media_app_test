package com.unware.mediaapp.ui.medialist

import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.unware.mediaapp.MediaApp
import com.unware.mediaapp.model.Audio
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import android.icu.lang.UCharacter.GraphemeClusterBreak.T




class MediaListViewModel : ViewModel(), CoroutineScope {

    private val job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    private var audioLiveData: MutableLiveData<List<Audio>> = MutableLiveData()
    fun getAudioList(): LiveData<List<Audio>> {
        return audioLiveData
    }

    private fun loadAllAudioFromDevice(): List<Audio> {

        val tempAudioList = mutableListOf<Audio>()

        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0 "  // WHERE QUERY
        val projection = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ALBUM_KEY,
            MediaStore.Audio.Media.ALBUM,
            /*MediaStore.Audio.Media.DURATION, FOR Android >= Q */
            MediaStore.Audio.Media.DATA, /* File path to Audio file */
            MediaStore.Audio.Media._ID  // context id/ uri id of the file
        )
        val sortOrder = MediaStore.Audio.AudioColumns.TITLE + " COLLATE LOCALIZED ASC"

        val c = MediaApp.applicationContext().contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            sortOrder  /* SORT BY*/
        )



        if (c != null) {

            c.moveToFirst()

            while (!c.isAfterLast) {
                val mediaId = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
                val path = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                val name = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
                val album = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM))
                val albumId = c.getLong(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))
                val albumKey = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_KEY))
                val artist = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))

                var albumArtPath = ""
                val albumCursor = MediaApp.applicationContext().contentResolver.query(
                    MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                    arrayOf(MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART),
                    MediaStore.Audio.Albums._ID + " =? ",
                    arrayOf(albumId.toString()),
                    null
                )
                if (albumCursor != null) {
                    albumCursor.moveToFirst()
                    albumArtPath = albumCursor.getString(
                                albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART)
                            ) ?: ""
                }

                println("Media Id: $mediaId albumId: $albumId, album: $album, albumArt: $albumArtPath")

                val audioModel = Audio(mediaId.toLong(), path, name, album, albumId, albumArtPath, artist)

                // println("Audio file: $audioModel")

                tempAudioList.add(audioModel)

                c.moveToNext()
            }
            c.close()
        }

        return tempAudioList
    }


    fun getAllAudio() {
        launch(Dispatchers.Main) {
            audioLiveData.value = withContext(Dispatchers.IO) {
                loadAllAudioFromDevice()
            }
        }
    }

}
