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

class GalleryLoadStateViewHolder(
    view: View,
    retry: () -> Unit
) : RecyclerView.ViewHolder(view) {

    var retryButton:Button?=null
    var progressBar:LottieAnimationView?=null
    var errorMsg:TextView?=null
    init {
         retryButton = view.findViewById<Button>(R.id.retryButton)
         progressBar = view.findViewById<LottieAnimationView>(R.id.progressBar)
         errorMsg    = view.findViewById<TextView>(R.id.errorMsg)
        retryButton?.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) errorMsg?.text = loadState.error.localizedMessage
        progressBar?.isVisible = loadState is LoadState.Loading
        retryButton?.isVisible = loadState !is LoadState.Loading
        errorMsg?.isVisible = loadState !is LoadState.Loading
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): GalleryLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_list_footer, parent, false)
            return GalleryLoadStateViewHolder(view, retry)
        }
    }
}