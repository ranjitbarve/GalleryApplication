package com.ranjit.galleryapplication.presentation.gallery.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ranjit.galleryapplication.R
import com.ranjit.galleryapplication.domain.model.Gallery
import com.ranjit.galleryapplication.presentation.utils.DateTimeUtils.convertMillisToDateTime
import com.ranjit.galleryapplication.presentation.utils.DateTimeUtils.convertMillisToDateTimeBelowAPI26

/*
Key points:

The class extends PagingDataAdapter and utilizes the DataDiff object for efficient item updates.
ViewHolder represents the view holder for the adapter.
onBindViewHolder method binds the data to the view holder and sets up click listeners.
getItemViewType method determines the view type based on the position.
onCreateViewHolder method creates a new view holder.
DataDiff object calculates differences between items for efficient updates.
*/

internal class GalleryAdapter(
    private val itemClick: (Gallery) -> Unit
) : PagingDataAdapter<Gallery, GalleryAdapter.ViewHolder>(DataDiff) {

    // Enum class representing different view types in the adapter.
    enum class ViewType {
        DATA,
        FOOTER
    }

    // ViewHolder class for the adapter.
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    // Lifecycle method: Called to bind the data to the view holder.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Retrieve the gallery item at the current position.
        val gallery: Gallery? = getItem(position)

        // Find views within the item layout.
        val textTitle = holder.itemView.findViewById<TextView>(R.id.textTitle)
        val textDateTime = holder.itemView.findViewById<TextView>(R.id.textDateTime)
        val textImageCount = holder.itemView.findViewById<TextView>(R.id.textImageCount)
        val imgGallery = holder.itemView.findViewById<AppCompatImageView>(R.id.imgGallery)

        // Set the title text.
        textTitle.text = gallery?.title

        // Set the datetime text if available, otherwise hide the view.
        if (gallery?.datetime == null) {
            textDateTime.isVisible = false
        } else {
            textDateTime.isVisible = true
            val dateTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                convertMillisToDateTime(gallery?.datetime)
            } else {
                convertMillisToDateTimeBelowAPI26(gallery?.datetime)
            }
            textDateTime.text = dateTime
        }

        // Set the image count text if available, otherwise hide the view.
        if (gallery?.imagesCount != null && gallery?.imagesCount > 0) {
            textDateTime.isVisible = true
            val imagesCount = gallery?.imagesCount?.toString()
            textImageCount.text = "Images Count : $imagesCount"
        } else {
            textDateTime.isVisible = false
            textImageCount.text = "Images Count : 0"
        }

        // Load the cover image using Glide.
        Glide.with(holder.itemView.context)
            .load(gallery?.coverUrl)
            .centerCrop()
            .thumbnail(0.5f)
            .placeholder(R.drawable.ic_launcher_background)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imgGallery)

        // Set click listener to handle item clicks.
        gallery?.let { it ->
            holder.itemView.setOnClickListener { _ ->
                itemClick.invoke(it)
            }
        }
    }

    // Lifecycle method: Called to determine the view type of the item at the specified position.
    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount) {
            ViewType.DATA.ordinal
        } else {
            ViewType.FOOTER.ordinal
        }
    }

    // Lifecycle method: Called to create a new ViewHolder.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_list_gallery, parent, false)
        )
    }

    // Object responsible for calculating the differences between two non-null items in a list.
    object DataDiff : DiffUtil.ItemCallback<Gallery>() {

        // Called to check whether two items represent the same object.
        override fun areItemsTheSame(oldItem: Gallery, newItem: Gallery): Boolean {
            return oldItem.id == newItem.id
        }

        // Called to check whether two items have the same data.
        override fun areContentsTheSame(oldItem: Gallery, newItem: Gallery): Boolean {
            return oldItem == newItem
        }
    }
}
