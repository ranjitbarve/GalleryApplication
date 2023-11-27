package com.ranjit.galleryapplication.presentation.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ranjit.galleryapplication.domain.model.Gallery
import com.ranjit.galleryapplication.domain.usecase.GalleryDataSource
import com.ranjit.galleryapplication.domain.usecase.GetGalleryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


/*
Key points:

@HiltViewModel: An annotation for Hilt to mark this class as a ViewModel that can be injected.
The class has properties to store default values and configurations for gallery data.
selectedGalleryItem is a LiveData holding the selected gallery item.
galleryPagingData is a LiveData holding the Flow of PagingData for the gallery.
Methods like getSelectedGalleryItem, selectGalleryItem, and getGalleryPagingData provide access to the LiveData.
The nowPlaying method is responsible for updating the gallery data based on specified parameters using the Paging library.
*/


@HiltViewModel
internal class GalleryViewModel @Inject constructor(
    private val getGalleryUseCase: GetGalleryUseCase
) : ViewModel() {

    // This ViewModel class is annotated with @HiltViewModel, indicating it's eligible for dependency injection through Hilt.

    // Properties to store default values and configurations for gallery data.
    private var section: String = "top"
    private var sortBy: String = "top"
    private var window: String = "week"
    private var showViral: Boolean = true
    private val pageSize = 20
    var lastViewType: GalleryFragment.ViewType = GalleryFragment.ViewType.GRID

    // LiveData to hold the selected gallery item.
    private val selectedGalleryItem = MutableLiveData<Gallery>()

    // LiveData to hold the Flow of PagingData for the gallery.
    private val galleryPagingData = MutableLiveData<Flow<PagingData<Gallery>>>()

    // Expose the selected gallery item as LiveData.
    fun getSelectedGalleryItem(): LiveData<Gallery> = selectedGalleryItem

    // Method to update the selected gallery item.
    fun selectGalleryItem(item: Gallery) {
        selectedGalleryItem.value = item
    }

    // Expose the gallery PagingData as LiveData.
    fun getGalleryPagingData(): LiveData<Flow<PagingData<Gallery>>> = galleryPagingData

    // Initialization block, called when the ViewModel is created.
    init {
        nowPlaying()
    }

    // Method to fetch and update the gallery data based on specified parameters.
    fun nowPlaying(
        section: String = this.section,
        sort: String = this.sortBy,
        window: String = this.window,
        showViral: Boolean = this.showViral
    ) {
        // Update the configuration parameters.
        this.section = section
        this.sortBy = sort
        this.window = window
        this.showViral = showViral

        // Set the LiveData with the PagingData flow using Pager.
        galleryPagingData.value = Pager(PagingConfig(pageSize = pageSize)) {
            GalleryDataSource(
                useCase = getGalleryUseCase,
                section = section,
                sort = sort,
                window = window,
                showViral = showViral
            )
        }.flow.cachedIn(viewModelScope)
    }
}
