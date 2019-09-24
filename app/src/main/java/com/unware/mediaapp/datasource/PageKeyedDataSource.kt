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

    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<String, Gallery>) {

        val queryParams = HashMap<String, String>()
        queryParams["api_key"] = "ZME7nTxOPIqXCf9qhCfqB1ICqtFCbCs5"
        queryParams["lang"] = "id"
        if (searchQuery.isNotEmpty()) queryParams["q"] = searchQuery
        queryParams["limit"] = params.requestedLoadSize.toString()
        queryParams["offset"] = "0"


        val initialRequest = if (searchQuery.isEmpty()) {
            apiRequest.giphyTrending(queryParams)
        } else {
            apiRequest.giphySearch(queryParams)
        }

        // update network states.
        // we also provide an initial load state to the listeners so that the UI can know when the
        // very first list is loaded.
        initialLoad.postValue(RequestStatus.LOADING)

        try {
            val response = initialRequest.execute()
            val gifList = response.body()?.data?.map { it } ?: emptyList()
            val prevPageKey = response.body()?.pagination?.offset
            val nextPageKey = response.body()?.pagination?.offset?.plus(Constants.SB_API_REQUEST_LIMIT)

            retry = null

            initialLoad.postValue(
                    if (gifList.isEmpty()) RequestStatus.NO_DATA
                    else RequestStatus.FINISHED
            )


            callback.onResult(
                    gifList,
                    prevPageKey.toString(),
                    nextPageKey.toString()
                    )

        } catch (iOEx: IOException) {
            retry = { loadInitial(params, callback) }
            initialLoad.postValue(RequestStatus.error(iOEx.message ?: "Initial load error"))
        }
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, GiphyImage>) {

        val queryParams = HashMap<String, String>()
        queryParams["api_key"] = "ZME7nTxOPIqXCf9qhCfqB1ICqtFCbCs5"
        queryParams["lang"] = "id"
        if (searchQuery.isNotEmpty()) queryParams["q"] = searchQuery
        queryParams["limit"] = Constants.SB_API_REQUEST_LIMIT.toString()
        queryParams["offset"] = params.key


        val initialRequest = if (searchQuery.isEmpty()) {
            apiRequest.giphyTrending(queryParams)
        } else {
            apiRequest.giphySearch(queryParams)
        }

        networkState.postValue(RequestStatus.LOADING)

        try {
            val response = initialRequest.execute()
            val gifList = response.body()?.data?.map { it } ?: emptyList()
            val nextPageKey = response.body()?.pagination?.offset

            retry = null

            callback.onResult(
                    gifList,
                    (nextPageKey?.plus(Constants.SB_API_REQUEST_LIMIT)).toString()
            )

            networkState.postValue(RequestStatus.FINISHED)

        } catch (iOEx: IOException) {
            retry = { loadAfter(params, callback) }
            networkState.postValue(RequestStatus.error(iOEx.message ?: "Load more error"))
        }
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, GiphyImage>) {
        // pass
    }
}