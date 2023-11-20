package com.sample.gallery.data.model

import DataFixtures
import com.ranjit.galleryapplication.data.model.toDomain
import org.junit.Assert.assertEquals
import org.junit.Test

class GalleryResponseDataTest {

    @Test
    fun `gallery response data to domain`() {
        val galleryResponseData = DataFixtures.getGalleryResponseData()
        val galleryResponse = DataFixtures.getGalleryResponse()

        assertEquals(galleryResponse, galleryResponseData.toDomain())
        assertEquals(galleryResponse.success, galleryResponseData.toDomain().success)
        assertEquals(galleryResponse.status, galleryResponseData.toDomain().status)
        assertEquals(galleryResponse.data, galleryResponseData.data.toDomain())
    }
}