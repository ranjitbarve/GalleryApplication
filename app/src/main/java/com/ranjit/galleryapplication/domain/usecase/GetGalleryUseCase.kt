package com.ranjit.galleryapplication.domain.usecase

import com.ranjit.galleryapplication.domain.model.GalleryResponse
import com.ranjit.galleryapplication.domain.repository.GalleryRepository

class GetGalleryUseCase(
    private val galleryRepository: GalleryRepository
) {

    // This class represents a use case for getting gallery data.
    // It takes a GalleryRepository as a dependency for retrieving the data.

    suspend fun getGallery(
        section: String,
        sort: String,
        window: String,
        page: Int,
        showViral: Boolean
    ): GalleryResponse = galleryRepository.getGallery(section, sort, window, page, showViral)

    // This function delegates the call to the corresponding function in the GalleryRepository.
    // It is marked as suspend since it interacts with the repository, which may involve asynchronous operations.

//    suspend fun getGallery(...): This function is marked as suspend since it interacts with the repository,
//    which may involve asynchronous operations. It delegates the call to the getGallery
//    function of the injected galleryRepository to retrieve the gallery data.

}

