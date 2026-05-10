package com.example.picsumgallery.utils

import com.example.picsumgallery.data.local.ImageEntity
import com.example.picsumgallery.data.remote.PicsumImageDto
import com.example.picsumgallery.domain.ImageItem

/**
 * Extension functions to map between Data Transfer Objects (DTO),
 * Room Entities, and Domain Models.
 */

/**
 * Converts a [PicsumImageDto] (API) to an [ImageEntity] (Room).
 * @param position The position of the item in the list for cache ordering.
 */
fun PicsumImageDto.toEntity(position: Int): ImageEntity {
    return ImageEntity(
        id = this.id,
        author = this.author,
        width = this.width,
        height = this.height,
        url = this.url,
        downloadUrl = this.downloadUrl,
        isFavorite = false, // Defaults to false on first fetch
        favoriteTimestamp = 0L,
        cacheTimestamp = System.currentTimeMillis(),
        cachePosition = position
    )
}

/**
 * Converts an [ImageEntity] (Room) to an [ImageItem] (Domain).
 */
fun ImageEntity.toDomain(): ImageItem {
    return ImageItem(
        id = this.id,
        author = this.author,
        width = this.width,
        height = this.height,
        url = this.url,
        downloadUrl = this.downloadUrl,
        isFavorite = this.isFavorite
    )
}
