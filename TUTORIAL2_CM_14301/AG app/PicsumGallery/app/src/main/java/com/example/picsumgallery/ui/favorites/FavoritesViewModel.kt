package com.example.picsumgallery.ui.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.picsumgallery.data.repository.ImageRepository
import com.example.picsumgallery.domain.ImageItem
import kotlinx.coroutines.launch

/**
 * ViewModel for the Favorites screen.
 */
class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ImageRepository(application)

    val favoritesLiveData: LiveData<List<ImageItem>> = repository.getFavoritesLive()

    /**
     * Toggles favorite status for an image.
     */
    fun toggleFavorite(id: String) {
        viewModelScope.launch {
            repository.toggleFavorite(id)
        }
    }
}
