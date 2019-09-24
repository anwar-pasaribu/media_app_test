package com.unware.mediaapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Gallery(
    val largeImageURL: String,
    val webformatHeight: Long,
    val webformatWidth: Long,
    val likes: Long,
    val imageWidth: Long,
    val id: Long,
    val userID: Long,
    val views: Long,
    val comments: Long,
    val pageURL: String,
    val imageHeight: Long,
    val webformatURL: String,
    val type: String,
    val previewHeight: Long,
    val tags: String,
    val downloads: Long,
    val user: String,
    val favorites: Long,
    val imageSize: Long,
    val previewWidth: Long,
    val userImageURL: String,
    val previewURL: String
): Parcelable