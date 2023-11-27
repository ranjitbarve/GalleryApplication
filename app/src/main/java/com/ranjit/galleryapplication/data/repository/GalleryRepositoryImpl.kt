package com.ranjit.galleryapplication.data.repository

import com.ranjit.data.base.SafeApiRequest
import com.ranjit.galleryapplication.data.model.toDomain
import com.ranjit.galleryapplication.data.remote.RetrofitService
import com.ranjit.galleryapplication.domain.model.GalleryResponse
import com.ranjit.galleryapplication.domain.repository.GalleryRepository
import javax.inject.Inject

internal class GalleryRepositoryImpl @Inject constructor(
    private val retrofitService: RetrofitService
) : GalleryRepository, SafeApiRequest() {

    // This class implements the GalleryRepository interface and is annotated with @Inject
    // to indicate that Hilt should provide the dependencies when creating an instance.

    override suspend fun getGallery(
        section: String,
        sort: String,
        window: String,
        page: Int,
        showViral: Boolean
    ): GalleryResponse =
        apiRequest { retrofitService.getGallery(section, sort, window, page, showViral) }.toDomain()

    // This function is part of the GalleryRepository interface and is responsible for
    // making a network request to get gallery data. It uses the apiRequest function from
    // the SafeApiRequest class to handle the network request safely.

}
