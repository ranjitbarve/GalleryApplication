package com.ranjit.galleryapplication.domain.usecase

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ranjit.galleryapplication.domain.model.Gallery

internal class GalleryDataSource(
    private val useCase: GetGalleryUseCase,
    private val section: String,
    private val sort: String,
    private val window: String,
    private val showViral: Boolean
) : PagingSource<Int, Gallery>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Gallery> {
        try {
            val currentLoadingPageKey = params.key ?: 1
            val response =
                useCase.getGallery(section, sort, window, currentLoadingPageKey, showViral)
            val responseData = mutableListOf<Gallery>()
            val data = response.data
            responseData.addAll(data)

            val prevKey = if (currentLoadingPageKey == 1) null else currentLoadingPageKey - 1

            return LoadResult.Page(
                data = responseData,
                prevKey = prevKey,
                nextKey = currentLoadingPageKey.plus(1)
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Gallery>): Int? {
        return null
    }
}