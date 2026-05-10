package com.example.picsumgallery.ui.main

import android.app.Application
import androidx.lifecycle.*
import com.example.picsumgallery.data.repository.ImageRepository
import com.example.picsumgallery.domain.ImageItem
import com.example.picsumgallery.domain.UiState
import kotlinx.coroutines.launch

/**
 * ViewModel for the main gallery screen.
 */
class GalleryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ImageRepository(application)

    private val _imagesLiveData = MutableLiveData<UiState<List<ImageItem>>>()
    val imagesLiveData: LiveData<UiState<List<ImageItem>>> = _imagesLiveData

    val favoritesLiveData: LiveData<List<ImageItem>> = repository.getFavoritesLive()

    private var currentPage = 1
    private var isFetching = false

    init {
        loadPage(1)
    }

    /**
     * Loads a specific page of images.
     */
    fun loadPage(page: Int, isRefresh: Boolean = false) {
        if (isFetching) return
        isFetching = true
        
        // Only show full-screen loading if it's the first page and not a refresh
        if (page == 1 && !isRefresh) {
            _imagesLiveData.value = UiState.Loading
        }

        viewModelScope.launch {
            try {
                val result = repository.fetchPage(page, isRefresh = isRefresh)
                _imagesLiveData.postValue(result)
                
                if (result is UiState.Success || result is UiState.Offline) {
                    currentPage = page
                }
            } catch (e: Exception) {
                _imagesLiveData.postValue(UiState.Error(e.message ?: "Unknown error"))
            } finally {
                isFetching = false
            }
        }
    }

    /**
     * Resets pagination and reloads with a random page to ensure fresh content.
     */
    fun refresh() {
        val randomPage = (1..50).random()
        loadPage(randomPage, isRefresh = true)
    }

    /**
     * Loads the next page for pagination.
     */
    fun loadNextPage() {
        loadPage(currentPage + 1)
    }

    /**
     * Toggles favorite status for an image.
     */
    fun toggleFavorite(id: String) {
        viewModelScope.launch {
            repository.toggleFavorite(id)
        }
    }
}
