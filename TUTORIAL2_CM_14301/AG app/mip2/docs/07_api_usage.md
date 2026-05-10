# 07 — API Usage

## API: Picsum Photos

**Base URL:** `https://picsum.photos`

No authentication or API key required.

---

## Endpoints Used

### 1. List Images (paginated)

```
GET /v2/list?page={page}&limit={limit}
```

| Parameter | Type | Description |
|-----------|------|-------------|
| `page`    | Int  | Page number, starting at 1 |
| `limit`   | Int  | Number of items per page (we use 20) |

**Example request:**
```
GET https://picsum.photos/v2/list?page=1&limit=20
```

**Example response:**
```json
[
  {
    "id": "0",
    "author": "Alejandro Escamilla",
    "width": 5616,
    "height": 3744,
    "url": "https://unsplash.com/photos/yC-Yzbqy7PY",
    "download_url": "https://picsum.photos/id/0/5616/3744"
  },
  {
    "id": "1",
    "author": "Alejandro Escamilla",
    "width": 5616,
    "height": 3744,
    "url": "https://unsplash.com/photos/LNRyGwIJr5c",
    "download_url": "https://picsum.photos/id/1/5616/3744"
  }
]
```

---

### 2. Display an Image (via Glide)

To show an image at a specific size, use:

```
https://picsum.photos/id/{id}/{width}/{height}
```

**Example:**
```
https://picsum.photos/id/0/400/300
```

This URL is passed directly to Glide — no Retrofit call needed for image display.

---

## Retrofit Interface

```kotlin
interface PicsumApiService {
    @GET("v2/list")
    suspend fun getImages(
        @Query("page") page: Int,
        @Query("limit") limit: Int = 20
    ): List<PicsumImageDto>
}
```

---

## Error Handling

| Scenario | Behaviour |
|----------|-----------|
| No internet | Serve from Room cache; show offline banner |
| HTTP 4xx / 5xx | Show error message + Retry button |
| Empty response | Show empty state message |
| Timeout | Show error message + Retry button |

All errors are mapped to `UiState.Error(message)` in the Repository layer.
