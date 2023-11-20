package com.ranjit.galleryapplication.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Processing (
	val status : String?
)