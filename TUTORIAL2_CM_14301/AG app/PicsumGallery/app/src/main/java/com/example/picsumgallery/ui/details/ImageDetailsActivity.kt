package com.example.picsumgallery.ui.details

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.picsumgallery.R
import com.example.picsumgallery.databinding.ActivityImageDetailsBinding
import com.example.picsumgallery.ui.shared.FavoritesStripAdapter

class ImageDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImageDetailsBinding
    private val viewModel: ImageDetailsViewModel by viewModels()
    private lateinit var favoritesAdapter: FavoritesStripAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageId = intent.getStringExtra("IMAGE_ID") ?: return
        
        setupToolbar()
        setupFavoritesStrip()
        observeViewModel()

        viewModel.loadImage(imageId)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupFavoritesStrip() {
        favoritesAdapter = FavoritesStripAdapter { item ->
            viewModel.loadImage(item.id)
        }
        binding.rvDetailsFavorites.adapter = favoritesAdapter
    }

    private fun observeViewModel() {
        viewModel.imageLiveData.observe(this) { item ->
            item?.let {
                binding.tvDetailAuthor.text = it.author
                binding.tvDimensions.text = "${it.width} x ${it.height}"
                
                Glide.with(this)
                    .load(it.downloadUrl)
                    .placeholder(android.R.color.darker_gray)
                    .into(binding.ivFullImage)

                // Update Favorite Button
                if (it.isFavorite) {
                    binding.btnToggleFavorite.text = "Remove from Favorites"
                    binding.btnToggleFavorite.setIconResource(R.drawable.ic_heart_filled)
                } else {
                    binding.btnToggleFavorite.text = "Add to Favorites"
                    binding.btnToggleFavorite.setIconResource(R.drawable.ic_heart_border)
                }

                binding.btnToggleFavorite.setOnClickListener { _ ->
                    viewModel.toggleFavorite(it.id)
                }
            }
        }

        viewModel.favoritesLiveData.observe(this) { favorites ->
            favoritesAdapter.submitList(favorites)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
