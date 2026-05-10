package com.example.picsumgallery.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.picsumgallery.data.local.AppDatabase
import com.example.picsumgallery.data.remote.RetrofitClient
import com.example.picsumgallery.domain.ImageItem
import com.example.picsumgallery.domain.UiState
import com.example.picsumgallery.utils.NetworkUtils
import com.example.picsumgallery.utils.toDomain
import com.example.picsumgallery.utils.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository that abstracts data sources (API and Room).
 * Acts as the single source of truth for the UI.
 */
class ImageRepository(private val context: Context) {

    private val apiService = RetrofitClient.apiService
    private val imageDao = AppDatabase.getDatabase(context).imageDao()

    /**
     * Fetches a page of images.
     * Tries the API first; if it fails or no network, falls back to the local cache.
     */
    suspend fun fetchPage(page: Int, limit: Int = 20, isRefresh: Boolean = false): UiState<List<ImageItem>> = withContext(Dispatchers.IO) {
        if (NetworkUtils.isNetworkAvailable(context)) {
            try {
                val dtos = apiService.getImages(page, limit)
                
                if (isRefresh) {
                    imageDao.clearCache()
                }

                // Calculate starting position for cache ordering
                val startPos = (page - 1) * limit
                val entities = dtos.mapIndexed { index, dto ->
                    dto.toEntity(startPos + index)
                }

                // Insert into Room
                entities.forEach { entity ->
                    // Check if already exists to preserve favorite status
                    val existing = imageDao.getById(entity.id)
                    if (existing != null) {
                        // Preserve isFavorite and favoriteTimestamp
                        val updated = entity.copy(
                            isFavorite = existing.isFavorite,
                            favoriteTimestamp = existing.favoriteTimestamp
                        )
                        imageDao.insertAll(listOf(updated))
                    } else {
                        imageDao.insertAll(listOf(entity))
                    }
                }
                
                // Maintain cache size (max 50 non-favorites) after bulk insert
                while (imageDao.getCacheCount() > 50) {
                    imageDao.deleteOldestCacheEntry()
                }

                val domainItems = imageDao.getAllCached().map { it.toDomain() }
                UiState.Success(domainItems)
            } catch (e: Exception) {
                // API error: Fallback to cache but flag as offline/error
                val cached = imageDao.getAllCached().map { it.toDomain() }
                if (cached.isNotEmpty()) {
                    UiState.Offline
                } else {
                    UiState.Error(e.message ?: "Unknown API error")
                }
            }
        } else {
            // No Network: Return cached data if available
            val cached = imageDao.getAllCached().map { it.toDomain() }
            if (cached.isNotEmpty()) {
                UiState.Offline
            } else {
                UiState.Error("No internet connection and no cached data.")
            }
        }
    }

    /**
     * Toggles the favorite status of an image.
     * Implements FIFO logic: maximum of 5 favorites allowed.
     */
    suspend fun toggleFavorite(id: String) = withContext(Dispatchers.IO) {
        val image = imageDao.getById(id) ?: return@withContext
        
        if (image.isFavorite) {
            // Unfavorite
            imageDao.setFavorite(id, false, 0L)
        } else {
            // Favorite: Check limit (FIFO)
            val currentFavoritesCount = imageDao.getFavorites().value?.size ?: 
                AppDatabase.getDatabase(context).imageDao().getAllCached().count { it.isFavorite } // Fallback for background thread
            
            if (currentFavoritesCount >= 5) {
                val oldest = imageDao.getOldestFavorite()
                if (oldest != null) {
                    imageDao.setFavorite(oldest.id, false, 0L)
                }
            }
            imageDao.setFavorite(id, true, System.currentTimeMillis())
        }
    }

    /**
     * Gets a single image by ID from the cache.
     */
    suspend fun getById(id: String): ImageItem? = withContext(Dispatchers.IO) {
        imageDao.getById(id)?.toDomain()
    }

    /**
     * Returns a LiveData of all favorites.
     */
    fun getFavoritesLive(): LiveData<List<ImageItem>> {
        return imageDao.getFavorites().map { list ->
            list.map { it.toDomain() }
        }
    }

    /**
     * Returns a LiveData of all cached images.
     */
    suspend fun getCachedImages(): List<ImageItem> = withContext(Dispatchers.IO) {
        imageDao.getAllCached().map { it.toDomain() }
    }
}
