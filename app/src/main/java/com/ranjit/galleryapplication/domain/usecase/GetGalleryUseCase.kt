package com.ranjit.galleryapplication.domain.usecase

import com.ranjit.galleryapplication.domain.model.GalleryResponse
import com.ranjit.galleryapplication.domain.repository.GalleryRepository

class GetGalleryUseCase(
    private val galleryRepository: GalleryRepository
) {

    suspend fun getGallery(
        section: String,
        sort: String,
        window: String,
        page: Int,
        showViral: Boolean
    ): GalleryResponse = galleryRepository.getGallery(section, sort, window, page, showViral)
}
