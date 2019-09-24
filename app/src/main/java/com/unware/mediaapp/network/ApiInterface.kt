package com.unware.mediaapp.network

import com.unware.mediaapp.model.GalleryResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface ApiInterface {

    @GET("/api/")
    fun getGalleryList(
        @QueryMap(encoded = true) queryParams: Map<String, String>
    ) : Call<GalleryResponse>
}