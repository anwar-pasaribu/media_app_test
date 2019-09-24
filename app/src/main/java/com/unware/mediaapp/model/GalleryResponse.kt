package com.unware.mediaapp.model

data class GalleryResponse(
    val totalHits: Int,
    val hits: List<Gallery>,
    val total: Int
)