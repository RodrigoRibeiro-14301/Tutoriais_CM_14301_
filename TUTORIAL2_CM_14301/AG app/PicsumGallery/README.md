# PicsumGallery

An Android photo gallery app built with Kotlin (XML Views, MVVM architecture).

## Features
- Browse photos from [picsum.photos](https://picsum.photos)
- Offline caching via Room (max 50 items)
- Favourites with FIFO queue (max 5)
- Pull-to-refresh, pagination, error/retry states

## Tech Stack
| Layer | Library |
|---|---|
| Networking | Retrofit 2 + Gson + OkHttp |
| Image loading | Glide 4 |
| Local DB | Room + KSP |
| Async | Kotlin Coroutines |
| UI State | ViewModel + LiveData |
| UI Components | RecyclerView, SwipeRefreshLayout |

## Package Structure
```
com.example.picsumgallery
├── data/local         Room entity, DAO, database
├── data/remote        Retrofit service, DTO
├── data/repository    ImageRepository (single source of truth)
├── domain             ImageItem, UiState
├── ui/main            MainActivity, GalleryViewModel, GalleryAdapter
├── ui/details         ImageDetailsActivity, ImageDetailsViewModel
├── ui/favorites       FavoritesActivity, FavoritesViewModel, FavoritesAdapter
├── ui/shared          FavoritesStripAdapter
└── utils              NetworkUtils, Mappers
```

## Min SDK
API 26 (Android 8.0)
