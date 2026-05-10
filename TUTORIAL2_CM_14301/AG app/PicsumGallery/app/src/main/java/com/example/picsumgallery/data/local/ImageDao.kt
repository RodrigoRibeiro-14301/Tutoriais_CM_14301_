package com.example.picsumgallery.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Data Access Object (DAO) for the images table.
 */
@Dao
interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(images: List<ImageEntity>)

    /** Returns all cached images ordered by their gallery position. */
    @Query("SELECT * FROM images ORDER BY cachePosition ASC")
    suspend fun getAllCached(): List<ImageEntity>

    /** Returns all favorites as LiveData for real-time UI updates. Ordered by favorite date. */
    @Query("SELECT * FROM images WHERE isFavorite = 1 ORDER BY favoriteTimestamp DESC")
    fun getFavorites(): LiveData<List<ImageEntity>>

    /** Toggles the favorite status and updates the timestamp for FIFO logic. */
    @Query("UPDATE images SET isFavorite = :isFavorite, favoriteTimestamp = :timestamp WHERE id = :id")
    suspend fun setFavorite(id: String, isFavorite: Boolean, timestamp: Long)

    /** Finds the oldest favorite item to implement the FIFO (max 5) rule. */
    @Query("SELECT * FROM images WHERE isFavorite = 1 ORDER BY favoriteTimestamp ASC LIMIT 1")
    suspend fun getOldestFavorite(): ImageEntity?

    /** Counts non-favorite items currently in the cache. */
    @Query("SELECT COUNT(*) FROM images WHERE isFavorite = 0")
    suspend fun getCacheCount(): Int

    /** Deletes the oldest non-favorite entry to maintain cache size (max 50). */
    @Query("DELETE FROM images WHERE id IN (SELECT id FROM images WHERE isFavorite = 0 ORDER BY cacheTimestamp ASC LIMIT 1)")
    suspend fun deleteOldestCacheEntry()

    /** Clears all non-favorite items from the cache. */
    @Query("DELETE FROM images WHERE isFavorite = 0")
    suspend fun clearCache()

    /** Fetches a single item by ID. */
    @Query("SELECT * FROM images WHERE id = :id")
    suspend fun getById(id: String): ImageEntity?
}
