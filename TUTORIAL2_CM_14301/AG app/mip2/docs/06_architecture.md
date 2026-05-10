# 06 — Architecture

## Pattern: MVVM (Model-View-ViewModel)

```
┌─────────────────────────────────────────────────────────┐
│                        UI Layer                         │
│   MainActivity / ImageDetailsActivity / FavoritesActivity│
│   XML Layouts / RecyclerView Adapters                   │
│              observes LiveData ↕                        │
├─────────────────────────────────────────────────────────┤
│                    ViewModel Layer                      │
│   GalleryViewModel / ImageDetailsViewModel              │
│   FavoritesViewModel                                    │
│              calls Repository ↕                        │
├─────────────────────────────────────────────────────────┤
│                   Repository Layer                      │
│   ImageRepository                                       │
│   Decides: fetch from API or serve from cache           │
│        ↕ API calls          ↕ DB calls                 │
├──────────────────────┬──────────────────────────────────┤
│    Remote Data Source│       Local Data Source          │
│    PicsumApiService  │       ImageDao (Room)            │
│    (Retrofit)        │       AppDatabase                │
└──────────────────────┴──────────────────────────────────┘
```

---

## Layer Responsibilities

### UI Layer
- Activities observe `LiveData<UiState<T>>` from ViewModels.
- No business logic in Activities or Adapters.
- Adapters only bind data to views.

### ViewModel Layer
- Holds and exposes `LiveData` to the UI.
- Calls `ImageRepository` using `viewModelScope` + Coroutines.
- Survives configuration changes.

### Repository Layer
- Single source of truth.
- On network available: fetch from API → save to Room → emit to ViewModel.
- On network unavailable: read from Room → emit to ViewModel with `UiState.Offline`.
- Manages cache eviction (max 50 non-favorites).
- Manages FIFO favorites logic (max 5).

### Remote Data Source
- `PicsumApiService` defined with Retrofit.
- Base URL: `https://picsum.photos/`
- Endpoints defined in `docs/07_api_usage.md`.

### Local Data Source
- Room database: `AppDatabase`
- Single table: `images` (`ImageEntity`)
- DAO: `ImageDao` with queries for CRUD, favorites, cache eviction.

---

## Package Structure

```
com.example.picsumgallery
├── data
│   ├── local
│   │   ├── AppDatabase.kt
│   │   ├── ImageDao.kt
│   │   └── ImageEntity.kt
│   ├── remote
│   │   ├── PicsumApiService.kt
│   │   └── PicsumImageDto.kt
│   └── repository
│       └── ImageRepository.kt
├── domain
│   ├── ImageItem.kt
│   └── UiState.kt
├── ui
│   ├── main
│   │   ├── MainActivity.kt
│   │   ├── GalleryViewModel.kt
│   │   └── GalleryAdapter.kt
│   ├── details
│   │   ├── ImageDetailsActivity.kt
│   │   └── ImageDetailsViewModel.kt
│   ├── favorites
│   │   ├── FavoritesActivity.kt
│   │   ├── FavoritesViewModel.kt
│   │   └── FavoritesAdapter.kt
│   └── shared
│       └── FavoritesStripAdapter.kt
└── utils
    ├── NetworkUtils.kt
    └── Mappers.kt
```
