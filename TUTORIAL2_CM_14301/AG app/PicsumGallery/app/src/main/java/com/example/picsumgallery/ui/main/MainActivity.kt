package com.example.picsumgallery.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.picsumgallery.R
import com.example.picsumgallery.databinding.ActivityMainBinding
import com.example.picsumgallery.domain.UiState
import com.example.picsumgallery.ui.details.ImageDetailsActivity
import com.example.picsumgallery.ui.favorites.FavoritesActivity
import com.example.picsumgallery.ui.shared.FavoritesStripAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: GalleryViewModel by viewModels()
    
    private lateinit var galleryAdapter: GalleryAdapter
    private lateinit var favoritesAdapter: FavoritesStripAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerViews()
        setupSwipeRefresh()
        observeViewModel()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
    }

    private fun setupRecyclerViews() {
        // Gallery Grid
        galleryAdapter = GalleryAdapter(
            onItemClick = { item ->
                val intent = Intent(this, ImageDetailsActivity::class.java).apply {
                    putExtra("IMAGE_ID", item.id)
                }
                startActivity(intent)
            },
            onFavoriteClick = { item ->
                viewModel.toggleFavorite(item.id)
            }
        )
        binding.rvGallery.adapter = galleryAdapter

        // Pagination Scroll Listener
        binding.rvGallery.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                    viewModel.loadNextPage()
                }
            }
        })

        // Favorites Strip
        favoritesAdapter = FavoritesStripAdapter(
            onFavoriteClick = { item ->
                val intent = Intent(this, ImageDetailsActivity::class.java).apply {
                    putExtra("IMAGE_ID", item.id)
                }
                startActivity(intent)
            }
        )
        binding.rvFavoritesStrip.adapter = favoritesAdapter
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun observeViewModel() {
        // Gallery Images State
        viewModel.imagesLiveData.observe(this) { state ->
            binding.swipeRefresh.isRefreshing = false
            binding.paginationProgressBar.visibility = View.GONE
            
            when (state) {
                is UiState.Loading -> {
                    if (galleryAdapter.itemCount == 0) {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.errorView.visibility = View.GONE
                    } else {
                        binding.paginationProgressBar.visibility = View.VISIBLE
                    }
                }
                is UiState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.errorView.visibility = View.GONE
                    binding.tvOfflineBanner.visibility = View.GONE
                    galleryAdapter.submitList(state.data)
                }
                is UiState.Offline -> {
                    binding.progressBar.visibility = View.GONE
                    binding.tvOfflineBanner.visibility = View.VISIBLE
                    // Offline state usually means we still show whatever is in the adapter/cache
                }
                is UiState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    if (galleryAdapter.itemCount == 0) {
                        binding.errorView.visibility = View.VISIBLE
                        binding.tvError.text = state.message
                    }
                }
            }
        }

        // Favorites
        viewModel.favoritesLiveData.observe(this) { favorites ->
            if (favorites.isEmpty()) {
                binding.rvFavoritesStrip.visibility = View.GONE
            } else {
                binding.rvFavoritesStrip.visibility = View.VISIBLE
                favoritesAdapter.submitList(favorites)
            }
        }

        binding.btnRetry.setOnClickListener {
            viewModel.refresh()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_favorites) {
            startActivity(Intent(this, FavoritesActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
