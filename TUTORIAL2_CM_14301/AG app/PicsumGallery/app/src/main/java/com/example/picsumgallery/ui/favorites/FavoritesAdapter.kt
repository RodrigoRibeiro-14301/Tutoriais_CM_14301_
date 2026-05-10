package com.example.picsumgallery.ui.favorites

import com.example.picsumgallery.ui.main.GalleryAdapter
import com.example.picsumgallery.domain.ImageItem

/**
 * Adapter for the Favorites screen. 
 * Reuses the logic from GalleryAdapter.
 */
class FavoritesAdapter(
    onItemClick: (ImageItem) -> Unit,
    onFavoriteClick: (ImageItem) -> Unit
) : GalleryAdapter(onItemClick, onFavoriteClick)
