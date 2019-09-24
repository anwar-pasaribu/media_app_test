package com.unware.mediaapp.ui.gallerylist

import androidx.lifecycle.ViewModel
import com.unware.mediaapp.model.Gallery
import com.unware.mediaapp.model.PagingDataListing
import com.unware.mediaapp.repositories.GalleryRepo

class GalleryListViewModel : ViewModel() {

    private val repo by lazy {
        GalleryRepo.getInstance()
    }

    private val galleryPagingData: PagingDataListing<Gallery> by lazy { repo.getList() }
    val galleryList = galleryPagingData.pagedList
    val loadMoreState = galleryPagingData.networkState
    val initialState = galleryPagingData.refreshState

    fun refresh() = galleryPagingData.refresh.invoke()
    fun retry() = galleryPagingData.retry.invoke()

}
