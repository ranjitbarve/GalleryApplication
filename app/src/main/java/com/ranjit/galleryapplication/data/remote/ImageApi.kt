package com.ranjit.galleryapplication.data.remote

object ImageApi {

  fun coverImageUrlFromId(id: String) = "https://i.imgur.com/$id.jpg"
}
