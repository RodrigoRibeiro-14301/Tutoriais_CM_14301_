package com.example.picsumgallery.ui.favorites

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.picsumgallery.databinding.ActivityFavoritesBinding
import com.example.picsumgallery.ui.details.ImageDetailsActivity

class FavoritesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritesBinding
    private val viewModel: FavoritesViewModel by viewModels()
    private lateinit var adapter: FavoritesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupRecyclerView() {
        adapter = FavoritesAdapter(
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
        binding.rvFavorites.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.favoritesLiveData.observe(this) { favorites ->
            if (favorites.isEmpty()) {
                binding.tvEmptyFavorites.visibility = View.VISIBLE
                binding.rvFavorites.visibility = View.GONE
            } else {
                binding.tvEmptyFavorites.visibility = View.GONE
                binding.rvFavorites.visibility = View.VISIBLE
                adapter.submitList(favorites)
            }
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
