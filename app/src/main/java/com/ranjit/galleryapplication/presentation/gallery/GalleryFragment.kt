package com.ranjit.galleryapplication.presentation.gallery

import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
import com.airbnb.lottie.LottieAnimationView
import com.ranjit.galleryapplication.R
import com.ranjit.galleryapplication.domain.model.Gallery
import com.ranjit.galleryapplication.presentation.gallery.adapter.GalleryAdapter
import com.ranjit.galleryapplication.presentation.gallery.adapter.GalleryLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GalleryFragment : Fragment() {
    private val itemClick: (Gallery) -> Unit = {
        findNavController().navigate(R.id.action_galleryFragment_to_galleryDetailFragment)
        galleryViewModel.selectGalleryItem(it)
    }

    private var rootView: View? = null
    private lateinit var galleryAdapter: GalleryAdapter

    private val galleryViewModel: GalleryViewModel by activityViewModels()

    var retryButton: Button?=null
    var recyclerViewGallery: RecyclerView?=null
    var progressBar: LottieAnimationView?=null
    var toolbar: Toolbar?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (null == rootView) {
            rootView = inflater.inflate(R.layout.fragment_gallery, container, false)
            galleryAdapter = GalleryAdapter(itemClick)
        }

        retryButton=rootView?.findViewById(R.id.retryButton)
        recyclerViewGallery = rootView?.findViewById(R.id.recyclerViewGallery)
        progressBar=rootView?.findViewById(R.id.progressBar)
        toolbar=rootView?.findViewById(R.id.toolbar)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
    }

    private fun setUI() {
        retryButton?.setOnClickListener { galleryAdapter.retry() }
        setupRecyclerView()
    }

    private fun setupObservers() {
        galleryViewModel.getGalleryPagingData()
            .observe(viewLifecycleOwner) { flow ->
                lifecycleScope.launch {
                    flow.collectLatest {
                        galleryAdapter.submitData(it)
                    }
                }
            }


    }

    private fun initAdapter(currentFilter: ViewType) {
        val spanCount = if (requireContext().resources.configuration.orientation ==
            Configuration.ORIENTATION_PORTRAIT
        ) 2 else 3

        recyclerViewGallery?.apply {
            when (currentFilter) {
                ViewType.GRID -> {
                    recyclerViewGallery?.layoutManager = GridLayoutManager(context, spanCount)
                    (layoutManager as GridLayoutManager).spanSizeLookup =
                        object : GridLayoutManager.SpanSizeLookup() {
                            override fun getSpanSize(position: Int): Int {
                                val viewType = galleryAdapter.getItemViewType(position)
                                return if (viewType == GalleryAdapter.ViewType.FOOTER.ordinal) 1
                                else spanCount
                            }
                        }
                }

                else -> {
                    recyclerViewGallery?.layoutManager = LinearLayoutManager(context)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        initAdapter(galleryViewModel.lastViewType)

        recyclerViewGallery?.apply {
            adapter = galleryAdapter.withLoadStateHeaderAndFooter(
                header = GalleryLoadStateAdapter { galleryAdapter.retry() },
                footer = GalleryLoadStateAdapter { galleryAdapter.retry() }
            )

            galleryAdapter.addLoadStateListener { loadState ->
                recyclerViewGallery!!.isVisible = loadState.source.refresh is LoadState.NotLoading
                progressBar?.isVisible = loadState.source.refresh is LoadState.Loading
                retryButton?.isVisible = loadState.source.refresh is LoadState.Error

                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                    ?: loadState.source.refresh as? LoadState.Error
                errorState?.let {
                    Toast.makeText(
                        context,
                        "\uD83D\uDE28 Wooops! ${it.error.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        setActionBar()
    }

    private fun setActionBar() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar!!)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.app_name)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_list -> {
                ViewType.LIST.let {
                    galleryViewModel.lastViewType = it
                    initAdapter(it)
                }
                true
            }
            R.id.action_grid -> {
                ViewType.GRID.let {
                    galleryViewModel.lastViewType = it
                    initAdapter(it)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    enum class ViewType {
        LIST,
        GRID
    }
}