package com.example.picsumgallery.data.remote

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object (DTO) for the Picsum API image response.
 *
 * This class matches the JSON structure returned by https://picsum.photos/v2/list.
 * It is used exclusively in the [data.remote] layer and is mapped to
 * [com.example.picsumgallery.domain.ImageItem] or [com.example.picsumgallery.data.local.ImageEntity]
 * before reaching the UI or database.
 *
 * Example JSON item:
 * {
 *   "id": "0",
 *   "author": "Alejandro Escamilla",
 *   "width": 5616,
 *   "height": 3744,
 *   "url": "https://unsplash.com/photos/yC-Yzbqy7PY",
 *   "download_url": "https://picsum.photos/id/0/5616/3744"
 * }
 */
data class PicsumImageDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("author")
    val author: String,
    @SerializedName("width")
    val width: Int,
    @SerializedName("height")
    val height: Int,
    @SerializedName("url")
    val url: String,
    @SerializedName("download_url")
    val downloadUrl: String
)
