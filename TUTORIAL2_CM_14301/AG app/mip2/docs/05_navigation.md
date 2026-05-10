# 05 — Navigation

## Navigation Map

```
MainActivity  (image grid + favorites strip)
    │
    ├──[tap image card]──────────────► ImageDetailsActivity
    │                                         │
    │                                         └──[back]──► MainActivity
    │
    └──[tap favorites icon in toolbar]──► FavoritesActivity
                                                │
                                                ├──[tap favorite thumbnail]──► ImageDetailsActivity
                                                │
                                                └──[back]──► MainActivity
```

---

## Navigation Details

### MainActivity → ImageDetailsActivity

- **Trigger:** User taps any image card in the grid.
- **How:** `Intent` with the `imageId: String` as an extra.
- **Back:** Standard back button returns to `MainActivity`.

```kotlin
// Example
val intent = Intent(this, ImageDetailsActivity::class.java)
intent.putExtra("IMAGE_ID", imageItem.id)
startActivity(intent)
```

---

### Any Screen → FavoritesActivity

- **Trigger:** User taps the heart/star icon in the Toolbar.
- **How:** `Intent` with no extras.
- **Back:** Standard back button returns to the calling screen.

---

### FavoritesActivity → ImageDetailsActivity

- **Trigger:** User taps a favorite image card.
- **How:** `Intent` with `imageId: String` as an extra (same as above).

---

### Favorites Strip (persistent, all screens)

- The horizontal strip at the top of every screen shows up to 5 favorite thumbnails.
- Tapping any thumbnail opens `ImageDetailsActivity` for that image directly.
- This strip is implemented as a shared `RecyclerView` + `FavoritesStripAdapter` included via `<include>` in each layout XML.
