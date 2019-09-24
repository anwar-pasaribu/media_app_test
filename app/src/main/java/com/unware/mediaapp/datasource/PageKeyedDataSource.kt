package com.unware.mediaapp.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.unware.mediaapp.model.Gallery
import com.unware.mediaapp.model.RequestStatus
import com.unware.mediaapp.network.ApiInterface
import java.io.IOException
import java.util.concurrent.Executor

class PageKeyedDataSource(
    private val apiRequest: ApiInterface,
    private val retryExecutor: Executor
) : PageKeyedDataSource<String, Gallery>() {

    companion object {
        private const val API_KEY = "10961674-bf47eb00b05f514cdd08f6e11"
    }

    // keep a function reference for the retry event
    private var retry: (() -> Any)? = null

    val networkState = MutableLiveData<RequestStatus>()

    val initialLoad = MutableLiveData<RequestStatus>()


    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            retryExecutor.execute {
                it.invoke()
            }
        }
    }

    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, Gallery>
    ) {

        val queryParams = mutableMapOf<String, String>()
        queryParams["key"] = API_KEY
        queryParams["page"] = "1"

        val initialRequest = apiRequest.getGalleryList(queryParams)

        // Initial Loading Indicator
        initialLoad.postValue(RequestStatus.loading("Initial load"))

        try {
            val response = initialRequest.execute()
            val gifList = response.body()?.hits?.map { it } ?: emptyList()
            val prevPageKey = "0"
            val nextPageKey = "2"

            retry = null

            initialLoad.postValue(
                if (gifList.isEmpty()) RequestStatus.nodata("No gallery")
                else RequestStatus.finished("Finished get first page")
            )

            callback.onResult(
                gifList,
                prevPageKey,
                nextPageKey
            )

        } catch (iOEx: IOException) {
            retry = { loadInitial(params, callback) }
            initialLoad.postValue(RequestStatus.error(iOEx.message ?: "Initial load error"))
        }
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, Gallery>) {

        val queryParams = HashMap<String, String>()
        queryParams["key"] = API_KEY
        queryParams["page"] = params.key

        val initialRequest = apiRequest.getGalleryList(queryParams)

        networkState.postValue(RequestStatus.loading("Loading more..."))

        try {
            val response = initialRequest.execute()
            val gifList = response.body()?.hits?.map { it } ?: emptyList()
            val nextPageKey = params.key.toIntOrNull()

            retry = null

            callback.onResult(
                gifList,
                (nextPageKey?.plus(1)).toString()
            )

            networkState.postValue(RequestStatus.finished("Page $nextPageKey loaded"))

        } catch (iOEx: IOException) {
            retry = { loadAfter(params, callback) }
            networkState.postValue(RequestStatus.error(iOEx.message ?: "Load more error"))
        }
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, Gallery>) {
        // pass
    }
}