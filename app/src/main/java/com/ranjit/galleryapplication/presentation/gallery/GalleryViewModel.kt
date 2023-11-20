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

@HiltViewModel
internal class GalleryViewModel @Inject constructor(
    private val getGalleryUseCase: GetGalleryUseCase
) : ViewModel() {

    private var section: String = "top"
    private var sortBy: String = "top"
    private var window: String = "week"
    private var showViral: Boolean = true
    private val pageSize = 20
    var lastViewType: GalleryFragment.ViewType = GalleryFragment.ViewType.GRID

    private val selectedGalleryItem = MutableLiveData<Gallery>()
    private val galleryPagingData = MutableLiveData<Flow<PagingData<Gallery>>>()

    fun getSelectedGalleryItem(): LiveData<Gallery> = selectedGalleryItem

    fun selectGalleryItem(item: Gallery) {
        selectedGalleryItem.value = item
    }

    fun getGalleryPagingData(): LiveData<Flow<PagingData<Gallery>>> = galleryPagingData

    init {
        nowPlaying()
    }

    fun nowPlaying(
        section: String = this.section,
        sort: String = this.sortBy,
        window: String = this.window,
        showViral: Boolean = this.showViral
    ) {
        this.section = section
        this.sortBy = sort
        this.window = window
        this.showViral = showViral

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