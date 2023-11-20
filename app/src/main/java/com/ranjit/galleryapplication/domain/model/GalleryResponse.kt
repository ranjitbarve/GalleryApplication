package com.ranjit.galleryapplication.domain.model

data class  GalleryResponse (

	val data : List<Gallery>,
	val success : Boolean,
	val status : Int
)
