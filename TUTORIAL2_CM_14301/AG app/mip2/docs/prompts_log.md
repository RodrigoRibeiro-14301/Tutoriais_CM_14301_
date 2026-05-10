# Prompts Log

> Log every significant AntiGravity prompt here. This helps track AI-assisted decisions and reflect on the development process.

---

## How to Add an Entry

```
## Prompt N
**Goal:** What you wanted to achieve.
**Prompt used:** The exact prompt sent to AntiGravity.
**Result:** Short description of what was generated and whether it worked.
```

---

## Prompt 0 — Step 1: Create Android Project Scaffold

**Goal:** Create the full Android project structure for PicsumGallery as specified in Step 1 of `08_implementation_plan.md`.

**Prompt used:**
```
Follow step 1 in 08_implementation plan.
Read docs/06_architecture.md and docs/08_implementation_plan.md.
Create a new Android project scaffold with:
- Language: Kotlin
- UI: Empty Views Activity (XML, not Compose)
- Package name: com.example.picsumgallery
- Min SDK: API 26
```

**Result:**
Created project at `/PicsumGallery/` with the following files:
- `settings.gradle.kts` — project name + repo config
- `build.gradle.kts` (root) — plugin declarations via version catalog
- `gradle/libs.versions.toml` — version catalog with all deps (Retrofit, OkHttp, Glide, Room+KSP, Coroutines, Lifecycle, SwipeRefreshLayout, RecyclerView)
- `app/build.gradle.kts` — app module config (minSdk=26, ViewBinding on, Compose off, all deps wired)
- `app/src/main/AndroidManifest.xml` — INTERNET + ACCESS_NETWORK_STATE, all 3 activities registered
- `app/src/main/java/.../ui/main/MainActivity.kt` — scaffold with ViewBinding, TODOs for Step 14
- `app/src/main/res/layout/activity_main.xml` — scaffold placeholder
- `app/src/main/res/values/strings.xml` — all app strings
- `app/src/main/res/values/colors.xml` — full color palette
- `app/src/main/res/values/dimens.xml` — spacing scale + component dimensions
- `app/src/main/res/values/themes.xml` — Material3 theme
- `gradle/wrapper/gradle-wrapper.properties` — Gradle 8.7
- `app/proguard-rules.pro` — rules for Retrofit, Gson, OkHttp, Glide, Room
- `README.md` — project overview

**Status:** ✅ Complete — ready to open in Android Studio

---

## Prompt 0b — Step 2: Add Dependencies (build.gradle)

**Goal:** Verify and confirm all required dependencies are declared in `app/build.gradle.kts` and `gradle/libs.versions.toml`.

**Prompt used:**
```
Now step 2 — verify app/build.gradle.kts contains all required dependencies:
Retrofit + Gson converter, OkHttp logging interceptor, Glide,
Room (runtime + KSP), Coroutines, ViewModel + LiveData, SwipeRefreshLayout, RecyclerView.
```

**Result:**
All dependencies were already fully wired in Step 1. Verified present in:
- `gradle/libs.versions.toml` — versions declared for all libs
- `app/build.gradle.kts` — all `implementation(...)` and `ksp(...)` calls confirmed

| Dependency | Version | Status |
|---|---|---|
| Retrofit + Gson | 2.11.0 | ✅ |
| OkHttp logging interceptor | 4.12.0 | ✅ |
| Glide | 4.16.0 | ✅ |
| Room runtime + KSP | 2.6.1 | ✅ |
| Coroutines | 1.9.0 | ✅ |
| ViewModel + LiveData | 2.8.7 | ✅ |
| SwipeRefreshLayout | 1.1.0 | ✅ |
| RecyclerView | 1.3.2 | ✅ |

**Status:** ✅ Complete — no changes needed, Step 1 already covered Step 2

---

## Prompt 1

**Goal:** Generate domain models.

**Prompt used:**
```
Read docs/04_data_model.md and docs/06_architecture.md.
Generate the file domain/ImageItem.kt and domain/UiState.kt as described.
Kotlin only. No Compose. Add comments.
```

**Result:**
Generated two files in `app/src/main/java/com/example/picsumgallery/domain/`:

- **`ImageItem.kt`** — data class with fields: `id`, `author`, `width`, `height`, `url`, `downloadUrl`, `isFavorite`. Fully documented with KDoc. Decoupled from Room and Retrofit.
- **`UiState.kt`** — covariant sealed class (`out T`) with four states: `Loading`, `Success<T>`, `Error(message)`, `Offline`. Each state has KDoc explaining when the UI should show it.

**Status:** ✅ Complete

---

## Prompt 2

**Goal:** Generate Retrofit API service.

**Prompt used:**
```
Read docs/07_api_usage.md and docs/06_architecture.md.
Generate data/remote/PicsumImageDto.kt and data/remote/PicsumApiService.kt.
Use Retrofit with suspend functions. Base URL: https://picsum.photos.
```

**Result:**
Generated three files in `app/src/main/java/com/example/picsumgallery/data/remote/`:
- **`PicsumImageDto.kt`** — Data class matching the API response, using GSON `@SerializedName` for mapping.
- **`PicsumApiService.kt`** — Retrofit interface with `suspend fun getImages(...)`.
- **`RetrofitClient.kt`** — Singleton providing the `apiService` instance with an `OkHttpClient` and `HttpLoggingInterceptor`.

**Status:** ✅ Complete

---

## Prompt 3

**Goal:** Generate Room database layer.

**Prompt used:**
```
Read docs/04_data_model.md and step 5 of docs/08_implementation_plan.md.
Generate:
- data/local/ImageEntity.kt
- data/local/ImageDao.kt
- data/local/AppDatabase.kt
Follow the fields and queries listed exactly.
```

**Result:**
Generated three files in `app/src/main/java/com/example/picsumgallery/data/local/`:
- **`ImageEntity.kt`** — Room entity with fields for API data and local metadata (`isFavorite`, `cacheTimestamp`, etc.).
- **`ImageDao.kt`** — DAO interface with methods for insertion, retrieval, and cache/favorite management (including FIFO logic queries).
- **`AppDatabase.kt`** — Singleton Room database class.

**Status:** ✅ Complete

---

## Prompt 4

**Goal:** Generate ImageRepository.

**Prompt used:**
```
Read docs/06_architecture.md and step 8 of docs/08_implementation_plan.md.
Generate data/repository/ImageRepository.kt.
It must: fetch from API, fallback to Room when offline, handle FIFO favorites (max 5), evict cache beyond 50 items.
Use coroutines. Handle all errors with try/catch.
```

**Result:**
Generated `data/repository/ImageRepository.kt`. 
- Implements `fetchPage` with API fetch and Room caching.
- Handles `UiState.Offline` and `UiState.Error`.
- Implements FIFO favorite logic (max 5) and cache eviction (max 50 non-favorites).
- Uses `Dispatchers.IO` for database and network operations.

**Status:** ✅ Complete

---

## Prompt 3b — Mappers & NetworkUtils (Steps 6 & 7)

**Goal:** Create utility classes for mapping and network status.

**Prompt used:**
```
Generate utils/Mappers.kt and utils/NetworkUtils.kt.
Mappers must handle DTO -> Entity and Entity -> Domain conversion.
NetworkUtils must check for active internet connection using ConnectivityManager.
```

**Result:**
Generated `utils/Mappers.kt` with extension functions and `utils/NetworkUtils.kt` with connectivity checks.

**Status:** ✅ Complete

---

## Prompt 5

**Goal:** Generate GalleryViewModel and wire MainActivity.

**Prompt used:**
```
Read docs/06_architecture.md, step 9 and step 14 of docs/08_implementation_plan.md.
Generate ui/main/GalleryViewModel.kt and complete ui/main/MainActivity.kt.
ViewModel must expose LiveData. Activity must observe and update UI accordingly.
```

**Result:**
Generated `ui/main/GalleryViewModel.kt` and updated `ui/main/MainActivity.kt`.
- ViewModel manages pagination, refresh, and favorite toggles.
- Activity implements `RecyclerView` with pagination listener, `SwipeRefreshLayout`, and handles all `UiState` transitions.

**Status:** ✅ Complete

---

## Prompt 6 — UI Details & Favorites (Steps 15 & 16)

**Goal:** Implement Image Details and Favorites screens.

**Prompt used:**
```
Follow steps 15 and 16 of the implementation plan.
Generate:
- res/layout/activity_image_details.xml
- ui/details/ImageDetailsViewModel.kt
- ui/details/ImageDetailsActivity.kt
- res/layout/activity_favorites.xml
- ui/favorites/FavoritesViewModel.kt
- ui/favorites/FavoritesActivity.kt
- ui/favorites/FavoritesAdapter.kt
```

**Result:**
Full implementation of both screens, including layouts, ViewModels, and activities. Integrated with existing Repository and domain models.

**Status:** ✅ Complete

---

## Prompt 7 — Final Polish (Step 17 & 18)

**Goal:** Ensure app is ready for testing.

**Prompt used:**
```
Check AndroidManifest.xml and add a way to navigate to Favorites from MainActivity.
```

**Result:**
- Verified `AndroidManifest.xml`.
- Added `res/menu/menu_main.xml`.
- Updated `MainActivity.kt` to handle menu and navigation.

**Status:** ✅ Complete

