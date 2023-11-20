package com.ranjit.galleryapplication.domain.repository

import com.ranjit.galleryapplication.domain.model.GalleryResponse


interface GalleryRepository {

    suspend fun getGallery(
        section: String,
        sort: String,
        window: String,
        page: Int,
        showViral: Boolean
    ): GalleryResponse
}