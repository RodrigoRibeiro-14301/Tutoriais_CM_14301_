package com.example.picsumgallery.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing an image stored in the local database.
 *
 * This table handles both the image cache (max 50 non-favorites) and
 * the permanent favorites list (max 5 items, FIFO).
 *
 * @property id                Unique ID from the Picsum API.
 * @property author            Photographer's name.
 * @property width             Original image width.
 * @property height            Original image height.
 * @property url               Unsplash page URL.
 * @property downloadUrl       Direct image URL used for display.
 * @property isFavorite        Whether the user has favorited this item.
 * @property favoriteTimestamp Timestamp when the item was favorited (used for FIFO favorites).
 * @property cacheTimestamp    Timestamp when the item was last cached (used for cache eviction).
 * @property cachePosition     The position/index of the item in the gallery (for ordering).
 */
@Entity(tableName = "images")
data class ImageEntity(
    @PrimaryKey val id: String,
    val author: String,
    val width: Int,
    val height: Int,
    val url: String,
    val downloadUrl: String,
    val isFavorite: Boolean = false,
    val favoriteTimestamp: Long = 0L,
    val cacheTimestamp: Long = 0L,
    val cachePosition: Int = 0
)
