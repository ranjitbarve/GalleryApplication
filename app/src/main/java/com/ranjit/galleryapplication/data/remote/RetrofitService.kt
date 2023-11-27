package com.ranjit.galleryapplication.data.remote


import com.ranjit.galleryapplication.data.model.GalleryResponseData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitService {

    // This interface defines the API endpoints using Retrofit annotations.

    @GET("gallery/{section}/{sort}/{window}/{page}")
    suspend fun getGallery(
        @Path("section") section: String,
        @Path("sort") sort: String,
        @Path("window") window: String,
        @Path("page") page: Int,
        @Query("showViral") showViral: Boolean
    ): Response<GalleryResponseData>

    // This function represents the endpoint for getting gallery data.
    // It uses the @GET annotation with the relative URL and @Path annotations for dynamic path segments.
    // @Query is used for query parameters, and suspend is used to perform the operation asynchronously.

}
