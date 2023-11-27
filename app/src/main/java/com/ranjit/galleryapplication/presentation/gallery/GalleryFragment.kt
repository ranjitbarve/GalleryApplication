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

/*
Key points:

The class is annotated with @AndroidEntryPoint for Hilt support.
UI components are initialized in onCreateView.
The adapter, ViewModel, and click listener are set up.
setUI method handles setting up the UI elements.
setupObservers observes changes in gallery data using LiveData.
setupRecyclerView initializes and sets up the RecyclerView with load state handling.
setActionBar sets up the ActionBar.
onOptionsItemSelected handles menu item selection for switching between list and grid views.
ViewType enum represents different gallery view types.
*/

@AndroidEntryPoint
class GalleryFragment : Fragment() {

    // Click listener for gallery items, navigating to the detail fragment and updating the selected item in the ViewModel.
    private val itemClick: (Gallery) -> Unit = {
        findNavController().navigate(R.id.action_galleryFragment_to_galleryDetailFragment)
        galleryViewModel.selectGalleryItem(it)
    }

    // Root view of the fragment.
    private var rootView: View? = null

    // ViewModel instance shared with the activity.
    private val galleryViewModel: GalleryViewModel by activityViewModels()

    // UI components
    var retryButton: Button? = null
    var recyclerViewGallery: RecyclerView? = null
    var progressBar: LottieAnimationView? = null
    var toolbar: Toolbar? = null

    // Adapter for the gallery items.
    private lateinit var galleryAdapter: GalleryAdapter

    // Lifecycle method: Called when the fragment is created.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    // Lifecycle method: Called to create and return the view hierarchy associated with the fragment.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (null == rootView) {
            rootView = inflater.inflate(R.layout.fragment_gallery, container, false)
            galleryAdapter = GalleryAdapter(itemClick)
        }

        // Initialize UI components.
        retryButton = rootView?.findViewById(R.id.retryButton)
        recyclerViewGallery = rootView?.findViewById(R.id.recyclerViewGallery)
        progressBar = rootView?.findViewById(R.id.progressBar)
        toolbar = rootView?.findViewById(R.id.toolbar)

        return rootView
    }

    // Lifecycle method: Called when the activity's onCreate() method has returned.
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupObservers()
    }

    // Lifecycle method: Called after onViewCreated(). Used for UI setup.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
    }

    // UI setup method.
    private fun setUI() {
        retryButton?.setOnClickListener { galleryAdapter.retry() }
        setupRecyclerView()
    }

    // Observes changes in gallery data using LiveData.
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

    // Initializes the adapter based on the current view type.
    private fun initAdapter(currentFilter: ViewType) {
        // Span count for grid layout.
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

    // Sets up the RecyclerView.
    private fun setupRecyclerView() {
        initAdapter(galleryViewModel.lastViewType)

        recyclerViewGallery?.apply {
            adapter = galleryAdapter.withLoadStateHeaderAndFooter(
                header = GalleryLoadStateAdapter { galleryAdapter.retry() },
                footer = GalleryLoadStateAdapter { galleryAdapter.retry() }
            )

            // Observes load state changes for showing/hiding UI elements and handling errors.
            galleryAdapter.addLoadStateListener { loadState ->
                recyclerViewGallery!!.isVisible = loadState.source.refresh is LoadState.NotLoading
                progressBar?.isVisible = loadState.source.refresh is LoadState.Loading
                retryButton?.isVisible = loadState.source.refresh is LoadState.Error

                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                    ?: loadState.source.refresh as? LoadState.Error

                // Show a toast message for errors.
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

    // Lifecycle method: Called when the fragment is visible to the user.
    override fun onResume() {
        super.onResume()
        setActionBar()
    }

    // Sets up the ActionBar.
    private fun setActionBar() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar!!)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.app_name)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(false)
    }

    // Lifecycle method: Called to inflate the options menu.
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    // Lifecycle method: Called when a menu item is selected.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_list -> {
                // Switch to the list view.
                ViewType.LIST.let {
                    galleryViewModel.lastViewType = it
                    initAdapter(it)
                }
                true
            }
            R.id.action_grid -> {
                // Switch to the grid view.
                ViewType.GRID.let {
                    galleryViewModel.lastViewType = it
                    initAdapter(it)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Enum class representing different view types (LIST and GRID).
    enum class ViewType {
        LIST,
        GRID
    }
}
