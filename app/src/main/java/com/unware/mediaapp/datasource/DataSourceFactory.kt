package com.unware.mediaapp.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.unware.mediaapp.model.Gallery
import com.unware.mediaapp.network.ApiInterface
import java.util.concurrent.Executor

class DataSourceFactory(
        private val apiRequest: ApiInterface,
        private val retryExecutor: Executor
) : DataSource.Factory<String, Gallery>() {

    val sourceLiveData = MutableLiveData<PageKeyedDataSource>()

    override fun create(): DataSource<String, Gallery> {
        val source = PageKeyedDataSource(
                apiRequest,
                retryExecutor
        )
        sourceLiveData.postValue(source)
        return source
    }
}
