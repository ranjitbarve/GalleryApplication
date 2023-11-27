package com.ranjit.galleryapplication.domain.repository

import com.ranjit.galleryapplication.domain.model.GalleryResponse


interface GalleryRepository {

    // This interface defines the contract for interacting with the gallery data.

    suspend fun getGallery(
        section: String,
        sort: String,
        window: String,
        page: Int,
        showViral: Boolean
    ): GalleryResponse

    // This function declares a suspend function to get gallery data.
    // The function takes parameters related to the desired gallery data (section, sort, window, page, and showViral).
    // It returns a GalleryResponse, representing the result of the operation.

//    suspend fun getGallery(...): This function is declared as suspend to allow for asynchronous execution.
}
