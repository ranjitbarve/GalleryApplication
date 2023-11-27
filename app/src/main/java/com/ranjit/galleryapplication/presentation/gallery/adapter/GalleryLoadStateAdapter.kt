package com.ranjit.galleryapplication.presentation.gallery.adapter

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

// LoadStateAdapter for displaying the load state of the gallery data.
class GalleryLoadStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<GalleryLoadStateViewHolder>() {

    // Lifecycle method: Called to bind the load state to the view holder.
    override fun onBindViewHolder(holder: GalleryLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    // Lifecycle method: Called to create a new view holder for the load state.
    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): GalleryLoadStateViewHolder {
        return GalleryLoadStateViewHolder.create(parent, retry)
    }
}
