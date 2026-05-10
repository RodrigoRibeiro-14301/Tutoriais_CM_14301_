# 04 — Data Model

## API Response Model

Returned by `https://picsum.photos/v2/list?page=1&limit=20`

```kotlin
data class PicsumImageDto(
    val id: String,
    val author: String,
    val width: Int,
    val height: Int,
    val url: String,
    val download_url: String
)
```

### Example JSON item

```json
{
  "id": "0",
  "author": "Alejandro Escamilla",
  "width": 5616,
  "height": 3744,
  "url": "https://unsplash.com/photos/yC-Yzbqy7PY",
  "download_url": "https://picsum.photos/id/0/5616/3744"
}
```

---

## Room Entity — Cached Image

Stored in the local database for offline access and cache management.

```kotlin
@Entity(tableName = "images")
data class ImageEntity(
    @PrimaryKey val id: String,
    val author: String,
    val width: Int,
    val height: Int,
    val url: String,
    val downloadUrl: String,
    val isFavorite: Boolean = false,
    val favoriteTimestamp: Long = 0L,   // used for FIFO ordering
    val cacheTimestamp: Long = 0L,      // used for eviction ordering
    val cachePosition: Int = 0          // scroll position index
)
```

---

## Domain Model — ImageItem

Used by the UI layer (ViewModels, Adapters). Decoupled from Room and API.

```kotlin
data class ImageItem(
    val id: String,
    val author: String,
    val width: Int,
    val height: Int,
    val url: String,
    val downloadUrl: String,
    val isFavorite: Boolean
)
```

---

## UI State Model

```kotlin
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
    object Offline : UiState<Nothing>()
}
```

---

## Favorites Queue Rules

- Maximum **5** favorites at any time.
- Stored as `isFavorite = true` in Room, ordered by `favoriteTimestamp ASC`.
- When a 6th item is added: the item with the **lowest** `favoriteTimestamp` is unfavorited (FIFO).

## Cache Rules

- Maximum **50** non-favorite items in the `images` table.
- Eviction strategy: remove the item with the **lowest** `cacheTimestamp` when the 51st item would be inserted.
- Favorites are **excluded** from this count and are never evicted automatically.
