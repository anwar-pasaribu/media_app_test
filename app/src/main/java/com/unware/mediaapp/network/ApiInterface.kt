package com.unware.mediaapp.network

import com.unware.mediaapp.model.GalleryResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {

    @GET("api/?key=10961674-bf47eb00b05f514cdd08f6e11&page={page}")
    fun getGalleryList(@Path("page") page: Int) : Call<GalleryResponse>
}