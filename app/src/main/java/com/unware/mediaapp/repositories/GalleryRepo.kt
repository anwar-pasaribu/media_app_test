package com.unware.mediaapp.repositories

import androidx.annotation.MainThread
import androidx.lifecycle.Transformations
import androidx.paging.Config
import androidx.paging.toLiveData
import com.unware.mediaapp.datasource.DataSourceFactory
import com.unware.mediaapp.model.Gallery
import com.unware.mediaapp.model.PagingDataListing
import com.unware.mediaapp.network.ApiClient
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class GalleryRepo {

    private val executors: ExecutorService by lazy {
        Executors.newFixedThreadPool(3)
    }

    @MainThread
    fun getList() : PagingDataListing<Gallery> {

        val gallerySourceFactory = DataSourceFactory(
            ApiClient.api,
            executors
        )

        val livePagedList = gallerySourceFactory.toLiveData(
            config = Config(
                pageSize = 50,
                enablePlaceholders = false,
                initialLoadSizeHint = 100
            ),
            fetchExecutor = executors
        )

        return PagingDataListing(
            pagedList = livePagedList,
            networkState = Transformations.switchMap(gallerySourceFactory.sourceLiveData) { it.networkState },
            retry = { gallerySourceFactory.sourceLiveData.value?.retryAllFailed() },
            refresh = { gallerySourceFactory.sourceLiveData.value?.invalidate() },
            refreshState = Transformations.switchMap(gallerySourceFactory.sourceLiveData) { it.initialLoad }
        )

    }

    companion object {

        // For Singleton instantiation
        private val LOCK = Any()
        private var sInstance: GalleryRepo? = null

        @Synchronized
        fun getInstance(): GalleryRepo {
            println("Getting repository")
            if (sInstance == null) {
                synchronized(LOCK) {
                    sInstance = GalleryRepo()
                    println("Made new repository")
                }
            }
            return sInstance!!
        }
    }

}