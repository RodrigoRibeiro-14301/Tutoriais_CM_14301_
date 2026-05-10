package com.example.picsumgallery.ui.details

import android.app.Application
import androidx.lifecycle.*
import com.example.picsumgallery.data.repository.ImageRepository
import com.example.picsumgallery.domain.ImageItem
import kotlinx.coroutines.launch

/**
 * ViewModel for the Image Details screen.
 */
class ImageDetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ImageRepository(application)

    private val _imageLiveData = MutableLiveData<ImageItem?>()
    val imageLiveData: LiveData<ImageItem?> = _imageLiveData

    val favoritesLiveData: LiveData<List<ImageItem>> = repository.getFavoritesLive()

    /**
     * Loads image details by ID.
     */
    fun loadImage(id: String) {
        viewModelScope.launch {
            val item = repository.getById(id)
            _imageLiveData.postValue(item)
        }
    }

    /**
     * Toggles favorite status for the current image.
     */
    fun toggleFavorite(id: String) {
        viewModelScope.launch {
            repository.toggleFavorite(id)
            // Reload after toggle to update UI
            loadImage(id)
        }
    }
}
