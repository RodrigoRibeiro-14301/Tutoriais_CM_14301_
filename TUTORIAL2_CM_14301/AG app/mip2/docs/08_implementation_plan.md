# 08 — Implementation Plan

> Follow this plan step by step. Do not skip steps. Generate one file at a time.

---

## Step 1 — Create Android Project

Create a new Android project in AntiGravity with:
- **Language:** Kotlin
- **UI:** Empty Views Activity (XML, not Compose)
- **Package name:** `com.example.picsumgallery`
- **Min SDK:** API 26

---

## Step 2 — Add Dependencies (build.gradle)

Add to `app/build.gradle`:
- Retrofit + Gson converter
- OkHttp logging interceptor
- Glide
- Room (runtime + KSP)
- Coroutines
- ViewModel + LiveData (lifecycle-extensions)
- SwipeRefreshLayout
- RecyclerView

---

## Step 3 — Create Domain Models

Generate these files:
- `domain/ImageItem.kt` — domain data class
- `domain/UiState.kt` — sealed class for UI states

---

## Step 4 — Create Remote Data Layer

Generate:
- `data/remote/PicsumImageDto.kt` — API response data class
- `data/remote/PicsumApiService.kt` — Retrofit interface with `getImages(page, limit)`
- A Retrofit singleton/object for building the service

---

## Step 5 — Create Local Data Layer (Room)

Generate:
- `data/local/ImageEntity.kt` — Room entity with all fields + `isFavorite`, `favoriteTimestamp`, `cacheTimestamp`, `cachePosition`
- `data/local/ImageDao.kt` — DAO with:
  - `insertAll(images)`
  - `getAllCached(): List<ImageEntity>`
  - `getFavorites(): LiveData<List<ImageEntity>>`
  - `setFavorite(id, isFavorite, timestamp)`
  - `getOldestFavorite(): ImageEntity?`
  - `getCacheCount(): Int`
  - `deleteOldestCacheEntry()`
  - `getById(id): ImageEntity?`
- `data/local/AppDatabase.kt` — Room database singleton

---

## Step 6 — Create Mappers

Generate `utils/Mappers.kt`:
- `PicsumImageDto.toEntity(position): ImageEntity`
- `ImageEntity.toDomain(): ImageItem`

---

## Step 7 — Create NetworkUtils

Generate `utils/NetworkUtils.kt`:
- Function `isNetworkAvailable(context): Boolean` using `ConnectivityManager`

---

## Step 8 — Create ImageRepository

Generate `data/repository/ImageRepository.kt`:
- `fetchPage(page): UiState<List<ImageItem>>` — tries API first, falls back to cache
- `toggleFavorite(id)` — handles FIFO queue (max 5 favorites)
- `getById(id): ImageItem?`
- `getFavoritesLive(): LiveData<List<ImageItem>>`
- Cache eviction logic (max 50 non-favorites)

---

## Step 9 — Create GalleryViewModel

Generate `ui/main/GalleryViewModel.kt`:
- `imagesLiveData: LiveData<UiState<List<ImageItem>>>`
- `favoritesLiveData: LiveData<List<ImageItem>>`
- `loadPage(page)` — calls repository
- `refresh()` — resets page and reloads
- `toggleFavorite(id)`

---

## Step 10 — Design activity_main.xml

Generate `res/layout/activity_main.xml`:
- `Toolbar`
- Favorites strip (`RecyclerView` horizontal, id: `rvFavoritesStrip`) — `GONE` when empty
- `SwipeRefreshLayout` wrapping:
  - `ProgressBar` (initial load, centered)
  - `RecyclerView` (grid, id: `rvGallery`)
  - Error view (`TextView` + `Button`)
- Pagination `ProgressBar` at the bottom

---

## Step 11 — Design item_image_card.xml

Generate `res/layout/item_image_card.xml`:
- `ImageView` (thumbnail, fixed height 180dp)
- `TextView` (author name, below image)
- Heart `ImageView` icon (top-right corner overlay)

---

## Step 12 — Create GalleryAdapter

Generate `ui/main/GalleryAdapter.kt`:
- `RecyclerView.Adapter` with `DiffUtil`
- Binds `ImageItem` to `item_image_card.xml`
- Loads images with Glide
- Exposes `onItemClick` and `onFavoriteClick` lambdas

---

## Step 13 — Create FavoritesStripAdapter

Generate `ui/shared/FavoritesStripAdapter.kt`:
- Horizontal `RecyclerView.Adapter`
- Shows up to 5 favorite thumbnails (60dp × 60dp, circular)
- Exposes `onFavoriteClick` lambda

---

## Step 14 — Wire MainActivity

Complete `ui/main/MainActivity.kt`:
- Set up Toolbar
- Observe `imagesLiveData` → show/hide loading, grid, error views
- Observe `favoritesLiveData` → show/hide favorites strip
- Set up `SwipeRefreshLayout` → calls `viewModel.refresh()`
- Set up pagination scroll listener → calls `viewModel.loadPage(nextPage)`
- Handle offline banner visibility

---

## Step 15 — Design & Implement ImageDetailsActivity

Generate:
- `res/layout/activity_image_details.xml` — full image + metadata + favorites strip
- `ui/details/ImageDetailsViewModel.kt` — loads item by ID, exposes `toggleFavorite`
- `ui/details/ImageDetailsActivity.kt` — receives `IMAGE_ID` intent extra, observes ViewModel

---

## Step 16 — Design & Implement FavoritesActivity

Generate:
- `res/layout/activity_favorites.xml` — grid + empty state + favorites strip
- `ui/favorites/FavoritesViewModel.kt` — exposes `getFavorites()` LiveData
- `ui/favorites/FavoritesActivity.kt` + `FavoritesAdapter.kt`

---

## Step 17 — AndroidManifest.xml

Update `AndroidManifest.xml`:
- Add `INTERNET` permission
- Add `ACCESS_NETWORK_STATE` permission
- Register `ImageDetailsActivity` and `FavoritesActivity`

---

## Step 18 — Final Testing Checklist

- [ ] Images load from API on first launch
- [ ] Pagination works when scrolling to the bottom
- [ ] Pull-to-refresh reloads images
- [ ] Tapping an image opens Image Details
- [ ] Favorite toggle works and strip updates instantly
- [ ] FIFO: adding a 6th favorite removes the oldest
- [ ] Offline: cached images shown with offline banner
- [ ] Error: retry button works after API failure
- [ ] Cache: only 50 non-favorite items stored
