package com.ranjit.galleryapplication.data.model

import com.ranjit.galleryapplication.domain.model.GalleryResponse
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GalleryResponseData(
    val data: List<Data>,
    val success: Boolean,
    val status: Int
)

internal fun GalleryResponseData.toDomain(): GalleryResponse = GalleryResponse(
	data = data.filter { it.id != null }.toDomain(),
	success = success,
	status = status
)