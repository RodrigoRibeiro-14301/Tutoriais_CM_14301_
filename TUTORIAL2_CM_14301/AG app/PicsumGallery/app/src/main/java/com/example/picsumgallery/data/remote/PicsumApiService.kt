package com.example.picsumgallery.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit interface for the Picsum Photos API.
 *
 * Documentation: https://picsum.photos
 * Base URL: https://picsum.photos/
 */
interface PicsumApiService {

    /**
     * Fetches a paginated list of images.
     *
     * @param page  The page number to retrieve (starts at 1).
     * @param limit The number of items per page (default is 20).
     * @return A list of [PicsumImageDto] items.
     */
    @GET("v2/list")
    suspend fun getImages(
        @Query("page") page: Int,
        @Query("limit") limit: Int = 20
    ): List<PicsumImageDto>
}
