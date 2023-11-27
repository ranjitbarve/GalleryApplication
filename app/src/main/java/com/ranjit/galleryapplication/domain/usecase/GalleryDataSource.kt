package com.ranjit.galleryapplication.domain.usecase

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ranjit.galleryapplication.domain.model.Gallery

/*Key points:

GalleryDataSource is a class that implements PagingSource for loading paginated gallery data.

load(params: LoadParams<Int>): LoadResult<Int, Gallery>: This function is called to load data
based on the provided LoadParams. It uses the GetGalleryUseCase to fetch the data
and constructs a LoadResult.Page with the loaded data, previous key, and next key.

getRefreshKey(state: PagingState<Int, Gallery>): Int?: This function is responsible
for providing a key for refreshing the data. In this case, null is returned, indicating that
the refresh key is not supported.*/

internal class GalleryDataSource(
    private val useCase: GetGalleryUseCase,
    private val section: String,
    private val sort: String,
    private val window: String,
    private val showViral: Boolean
) : PagingSource<Int, Gallery>() {

    // This class serves as a PagingSource for the gallery data.

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Gallery> {
        try {
            // The load function is responsible for loading a chunk of data based on the provided LoadParams.

            val currentLoadingPageKey = params.key ?: 1

            // Retrieve gallery data using the GetGalleryUseCase.
            val response = useCase.getGallery(section, sort, window, currentLoadingPageKey, showViral)
            val responseData = mutableListOf<Gallery>()
            val data = response.data
            responseData.addAll(data)

            // Calculate the previous and next page keys.
            val prevKey = if (currentLoadingPageKey == 1) null else currentLoadingPageKey - 1

            return LoadResult.Page(
                data = responseData,
                prevKey = prevKey,
                nextKey = currentLoadingPageKey.plus(1)
            )
        } catch (e: Exception) {
            // Handle any errors that might occur during data loading.
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Gallery>): Int? {
        // This function is responsible for providing a key for refreshing the data.
        // In this case, null is returned, indicating that the refresh key is not supported.
        return null
    }
}
