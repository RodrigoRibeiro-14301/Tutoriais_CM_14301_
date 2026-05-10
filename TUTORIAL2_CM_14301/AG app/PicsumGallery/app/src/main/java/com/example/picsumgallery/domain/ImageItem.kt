package com.example.picsumgallery.domain

/**
 * Domain model representing a single photo item.
 *
 * This class is the "contract" between the data layer and the UI layer.
 * It is deliberately decoupled from:
 *  - [com.example.picsumgallery.data.remote.PicsumImageDto] (API response shape)
 *  - [com.example.picsumgallery.data.local.ImageEntity] (Room persistence shape)
 *
 * ViewModels and Adapters always work with [ImageItem], never directly with
 * DTOs or Room entities. Mapping is handled in utils/Mappers.kt (Step 6).
 *
 * @param id           Unique identifier from the Picsum API (e.g. "0")
 * @param author       Photographer's name (e.g. "Alejandro Escamilla")
 * @param width        Original image width in pixels
 * @param height       Original image height in pixels
 * @param url          Unsplash page URL for the photo
 * @param downloadUrl  Direct image URL — used by Glide for loading thumbnails
 * @param isFavorite   Whether the user has marked this image as a favourite
 */
data class ImageItem(
    val id: String,
    val author: String,
    val width: Int,
    val height: Int,
    val url: String,
    val downloadUrl: String,
    val isFavorite: Boolean
)
