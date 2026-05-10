# 09 — Feature Extensions

> These features extend the base implementation plan. Follow the same rule: update this doc first, commit, then implement step by step.

---

## Extension 1 — MVVM Pattern (Required if not already in base)

**Status:** ✅ Included in base plan (Step 9, 15, 16)

Already enforced via `docs/06_architecture.md`. No additional steps needed.

---

## Extension 2 — Loading Indicator (Relative to Load Type)

**Description:** The loading indicator must reflect *what* is being loaded, not just a generic spinner.

### UI Changes
- Initial load → full-screen centered `ProgressBar`
- Pagination → small spinner at bottom of RecyclerView
- Swipe refresh → `SwipeRefreshLayout` built-in spinner
- Image detail load → spinner inside the `ImageView` while Glide loads

### Implementation Tasks
1. In `GalleryViewModel`, add `isInitialLoad: Boolean` flag.
2. In `activity_main.xml`, add both a full-screen `ProgressBar` and a footer `ProgressBar`.
3. In `GalleryAdapter`, add a footer item type that shows the pagination spinner.
4. In `ImageDetailsActivity`, use Glide's `.listener()` to show/hide a `ProgressBar` over the `ImageView`.

---

## Extension 3 — Image Details Screen

**Description:** A dedicated screen with full image and metadata.

### UI Changes
- Full-size `ImageView` (fills width, auto height)
- Author name (`TextView`)
- Dimensions (`TextView`: "5616 × 3744")
- Original URL (`TextView`, copyable on long press)
- Favorite toggle button in Toolbar (heart icon, filled = favorited)
- Persistent favorites strip at top

### Implementation Tasks
1. Generate `activity_image_details.xml` (Step 15 of base plan).
2. Generate `ImageDetailsViewModel.kt` — loads image by ID from Room.
3. Generate `ImageDetailsActivity.kt` — receives intent extra `IMAGE_ID`.
4. Add `copyToClipboard` on long press of URL text.
5. Register in `AndroidManifest.xml`.

---

## Extension 4 — Favorite Items (FIFO Queue, max 5)

**Description:** Users can save up to 5 favorite images. Oldest is removed when a 6th is added.

### UI Changes
- Heart icon on each card (filled = favorited)
- Persistent horizontal strip at the top of every screen (up to 5 thumbnails)
- Strip is hidden when there are no favorites

### Implementation Tasks
1. Add `isFavorite` and `favoriteTimestamp` to `ImageEntity` (Step 5).
2. Implement `toggleFavorite(id)` in `ImageRepository`:
   - If adding and count == 5 → unfavorite the oldest by `favoriteTimestamp`.
   - Update `isFavorite` and `favoriteTimestamp` in Room.
3. Create `FavoritesStripAdapter.kt` (Step 13).
4. Include strip layout in all three activity XML files via `<include>`.
5. All ViewModels observe `getFavoritesLive()` from Repository.

---

## Extension 5 — Cache (max 50 items, 10-ahead / 10-behind window)

**Description:** Keep up to 50 non-favorite images in Room. During scrolling, preload 10 ahead and keep 10 behind.

### Implementation Tasks
1. Add `cacheTimestamp` and `cachePosition` to `ImageEntity`.
2. In `ImageRepository.fetchPage()`:
   - After inserting new items, check `getCacheCount()`.
   - If count > 50, call `deleteOldestCacheEntry()` until count == 50.
3. In `GalleryViewModel`, implement a `prefetchWindow(currentPosition)` function:
   - Triggers `loadPage()` when within 10 items of the loaded edge.
4. In `MainActivity`, attach a `RecyclerView.OnScrollListener` that calls `prefetchWindow(lastVisibleItemPosition)`.
5. Loading indicators for prefetch use the pagination spinner only (not full-screen).

---

## Extension 6 — Offline Access

**Description:** When there is no internet, serve content from Room cache. Always allow access to favorites offline.

### Implementation Tasks
1. Create `NetworkUtils.kt` with `isNetworkAvailable(context): Boolean` (Step 7).
2. In `ImageRepository.fetchPage()`:
   - If offline → query Room → emit `UiState.Offline` with cached data.
3. In `MainActivity`:
   - Observe state; when `UiState.Offline`, show orange offline banner at top.
   - Hide banner when network returns (use `ConnectivityManager` callback).
4. Favorites are stored in Room and always available regardless of network state.

---

## Extension 7 — Error Handling

**Description:** Catch all API errors and show a friendly message with a Retry button.

### Implementation Tasks
1. In `ImageRepository`, wrap all Retrofit calls in `try/catch`.
2. Map exceptions to `UiState.Error(message)`:
   - `IOException` → "No internet connection"
   - `HttpException` → "Server error (code)"
   - Other → "Something went wrong"
3. In `MainActivity`, observe `UiState.Error`:
   - Hide RecyclerView and show error `TextView` + `Button`.
   - Retry button calls `viewModel.refresh()`.
4. In `ImageDetailsActivity`, show a `Toast` on error (since there's no list to replace).
