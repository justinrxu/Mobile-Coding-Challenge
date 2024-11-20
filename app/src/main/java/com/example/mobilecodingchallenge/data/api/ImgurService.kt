package com.example.mobilecodingchallenge.data.api;

import com.example.mobilecodingchallenge.data.entity.DataResponse
import com.example.mobilecodingchallenge.data.entity.GallerySearchResult
import retrofit2.http.GET;
import retrofit2.http.Path
import retrofit2.http.Query

interface ImgurService {
    @GET("/3/gallery/search/{sort}/{window}/{page}")
    suspend fun getGallerySearchResults(
        @Path("sort") sort: String,
        @Path("window") window: String,
        @Path("page") page: Int,
        @Query("q") query: String
    ): DataResponse<List<GallerySearchResult>>
}
