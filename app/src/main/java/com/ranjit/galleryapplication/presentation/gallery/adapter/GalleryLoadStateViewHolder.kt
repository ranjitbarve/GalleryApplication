package com.ranjit.galleryapplication.presentation.gallery.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.ranjit.galleryapplication.R

/*
Key points:

The class represents a view holder for displaying the load state of the gallery data.
Views within the item layout are initialized in the initialization block.
The bind method is used to bind the load state to the view holder and update view visibility accordingly.
The create method in the companion object is a factory method to create a new instance of the view holder.
The retry action is handled through the lambda expression retry.
*/

// ViewHolder class for displaying the load state of the gallery data.
class GalleryLoadStateViewHolder(
    view: View,
    retry: () -> Unit
) : RecyclerView.ViewHolder(view) {

    // Views within the item layout.
    var retryButton: Button? = null
    var progressBar: LottieAnimationView? = null
    var errorMsg: TextView? = null

    // Initialization block: Initialize views and set up click listener for the retry button.
    init {
        retryButton = view.findViewById<Button>(R.id.retryButton)
        progressBar = view.findViewById<LottieAnimationView>(R.id.progressBar)
        errorMsg = view.findViewById<TextView>(R.id.errorMsg)

        // Set click listener to handle retry action.
        retryButton?.setOnClickListener { retry.invoke() }
    }

    // Method to bind the load state to the view holder.
    fun bind(loadState: LoadState) {
        // Display error message if the load state is an error.
        if (loadState is LoadState.Error) errorMsg?.text = loadState.error.localizedMessage

        // Set visibility of views based on the load state.
        progressBar?.isVisible = loadState is LoadState.Loading
        retryButton?.isVisible = loadState !is LoadState.Loading
        errorMsg?.isVisible = loadState !is LoadState.Loading
    }

    // Companion object with a factory method to create a new instance of the view holder.
    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): GalleryLoadStateViewHolder {
            // Inflate the item layout for the load state.
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_footer, parent, false)

            // Create a new instance of the view holder.
            return GalleryLoadStateViewHolder(view, retry)
        }
    }
}
