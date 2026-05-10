package com.example.picsumgallery.domain

/**
 * Sealed class representing all possible UI states for any data-driven screen.
 *
 * Used as the wrapper type for LiveData exposed by ViewModels:
 *   LiveData<UiState<List<ImageItem>>>
 *
 * The UI layer (Activities) switches on these states to show/hide
 * the appropriate views (spinner, grid, error, offline banner).
 *
 * States:
 *  [Loading]  — an async operation is in progress (show spinner, hide content)
 *  [Success]  — data loaded successfully (hide spinner, show content)
 *  [Error]    — operation failed with a message (show error view + retry button)
 *  [Offline]  — no network; cached data may have been returned alongside this state
 *
 * Using `out T` (covariant) allows [Loading] and [Error] to be used where
 * UiState<List<ImageItem>> is expected, without requiring a type parameter.
 */
sealed class UiState<out T> {

    /** An async operation is in progress. No data available yet. */
    object Loading : UiState<Nothing>()

    /**
     * Operation completed successfully.
     * @param data The result payload — e.g. List<ImageItem>
     */
    data class Success<T>(val data: T) : UiState<T>()

    /**
     * Operation failed.
     * @param message A human-readable error description shown on the error view.
     */
    data class Error(val message: String) : UiState<Nothing>()

    /**
     * Device is offline. The repository may emit this alongside cached data.
     * The UI should display an offline banner and show whatever cached content
     * was loaded from Room.
     */
    object Offline : UiState<Nothing>()
}
