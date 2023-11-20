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

internal class GalleryAdapter(
    private val itemClick: (Gallery) -> Unit
) : PagingDataAdapter<Gallery, GalleryAdapter.ViewHolder>(DataDiff) {

    enum class ViewType {
        DATA,
        FOOTER
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val gallery: Gallery? = getItem(position)
        val textTitle = holder.itemView.findViewById<TextView>(R.id.textTitle)
        val textDateTime = holder.itemView.findViewById<TextView>(R.id.textDateTime)
        val textImageCount = holder.itemView.findViewById<TextView>(R.id.textImageCount)
        val imgGallery = holder.itemView.findViewById<AppCompatImageView>(R.id.imgGallery)
        textTitle.text = gallery?.title

        if (gallery?.datetime == null) {
            textDateTime.isVisible = false
        }else{
            textDateTime.isVisible = true
            var dateTime = "N/A"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                dateTime = convertMillisToDateTime(gallery?.datetime)
            }else{
                dateTime = convertMillisToDateTimeBelowAPI26(gallery?.datetime)
            }
            textDateTime.text = dateTime
        }

        if (gallery?.imagesCount!=null && gallery?.imagesCount>0){
            textDateTime.isVisible = true
            val imagesCount = gallery?.imagesCount?.toString()
            textImageCount.text = "Images Count : $imagesCount"
        }else{
            textDateTime.isVisible = false
            textImageCount.text = "Images Count : 0"
        }

        Glide.with(holder.itemView.context)
            .load(gallery?.coverUrl)
            .centerCrop()
            .thumbnail(0.5f)
            .placeholder(R.drawable.ic_launcher_background)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imgGallery)

        gallery?.let { it ->
            holder.itemView.setOnClickListener { _ ->
                itemClick.invoke(it)
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount) {
            ViewType.DATA.ordinal
        } else {
            ViewType.FOOTER.ordinal
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_list_gallery, parent, false)
        )
    }

    object DataDiff : DiffUtil.ItemCallback<Gallery>() {

        override fun areItemsTheSame(oldItem: Gallery, newItem: Gallery): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Gallery, newItem: Gallery): Boolean {
            return oldItem == newItem
        }
    }
}
