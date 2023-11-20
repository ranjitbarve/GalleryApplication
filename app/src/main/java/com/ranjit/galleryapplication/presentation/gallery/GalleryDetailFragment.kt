package com.ranjit.galleryapplication.presentation.gallery


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ranjit.galleryapplication.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryDetailFragment : Fragment() {
    private val viewModel: GalleryViewModel by activityViewModels()

    var toolbar:Toolbar?=null
    var textTitle: TextView?=null
    var textDesc: TextView?=null
    var imageGallery: ImageView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (activity is AppCompatActivity) {
                        findNavController().navigateUp()
                    }
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.fragment_gallery_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar=view.findViewById(R.id.toolbar)
        textTitle=view.findViewById(R.id.textTitle)
        textDesc=view.findViewById(R.id.textDesc)
        imageGallery=view.findViewById(R.id.imageGallery)

        setActionBar()
        initUI()
    }

    private fun setActionBar() {
//        var toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            (activity as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(true)
        }
    }

    private fun initUI() {
        viewModel.getSelectedGalleryItem().observe(viewLifecycleOwner, { gallery ->

            (activity as AppCompatActivity).supportActionBar?.title = gallery.title
            textTitle?.text = gallery.title
            textDesc?.text = gallery.description

            Glide.with(this)
                .load(gallery.coverUrl)
                .centerInside()
                .thumbnail(0.5f)
                .placeholder(R.drawable.ic_launcher_background)
                .centerInside()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageGallery!!)
        })
    }
}