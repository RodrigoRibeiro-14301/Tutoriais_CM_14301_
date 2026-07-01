# Tutorial 4 — Kotlin Flows, AI LLMs & Firebase

Mobile Computing (CM) — ENIDH
Based on `ENIDH_CM_Tutorial4_2026.pdf` (Pedro Fazenda, Carlos Gonçalves)

This repository documents the work completed for Tutorial 4, covering three areas: Kotlin coroutines/Flows, accessing AI language models (Gemini/OpenAI) from Kotlin, and building Android apps on top of Google Firebase.

For where each project physically lives, see [`INDEX.md`](./INDEX.md).

---

## Section 2 — Kotlin Flows

### 2.2 Coroutines/Flows tutorial
Worked through the official [Kotlin "Coroutines and Channels" tutorial](https://kotlinlang.org/docs/coroutines-and-channels.html) inside `intro_coroutines/`, covering all eight request-handling variants: Blocking, Background, Callbacks, Suspend, Concurrent, Not Cancellable, Progress, and Channels.

- Fixed a bug in `Request2Background.kt` where the background thread's result was discarded instead of being passed to `updateResults`.
- Fixed a race condition in `Request3Callbacks.kt` using `AtomicInteger` and `Collections.synchronizedList`.
- Implemented the remaining `TODO()` stubs (`Request4Suspend.kt`, `Request5Concurrent.kt`, `Request5NotCancellable.kt`, `Request6Progress.kt`, `Request7Channels.kt`) and the contributor `Aggregation.kt` logic.

### 2.3 — Reactive loading state with StateFlow
Copied the project to `intro-coroutinesV2/` and:

- Added a `LoadingStateData` data class (with `INIT`, `IN_PROGRESS`, `COMPLETED`, `CANCELED` states) alongside the existing `LoadingStatus` enum in `Contributors.kt`.
- Added a `StateFlow<LoadingStateData>` property to the `Contributors` interface, backed by a private `MutableStateFlow` in `ContributorsUI.kt` (backing-property pattern).
- Implemented `updateLoadingStatus`, `observeLoadingStatus`, `calculateElapsedTime`, and `updateResults` so loading status changes flow reactively into the UI instead of being set imperatively.

### 2.4 — Robust progress updates with Channels
Reworked the `CHANNELS` branch of `loadContributors()` in `Contributors.kt` (still in `intro-coroutinesV2/`) so that instead of calling `updateResults` directly on every received item, results are pushed through a second `Channel<Pair<List<User>, Boolean>>`. A separate coroutine consumes that channel and calls `updateResults` on the `Main` dispatcher — giving the UI natural back-pressure handling and cleaner cancellation.

---

## Section 3 — Accessing AI LLMs

### 3.1 — Get AISimpleCalls running
Configured `AISimpleCalls/` (a Kotlin console app) with a Gemini API key in `config.properties`, and tested all four access modes:

- `GEMINI` / `OPENAI` — manually built JSON requests via `org.json`
- `GEMINI-CLASSES` / `OPENAI-CLASSES` — typed Gson data classes for request/response serialization

Model was updated from the deprecated `gemini-2.0-flash` to the current `gemini-2.5-flash` after diagnosing a `429` quota error.

### 3.2 — Temperature & max tokens as config properties
Added `TEMPERATURE` and `MAX_TOKENS` as optional keys in `config.properties`, read through `AIAssistant`'s `temperature`/`maxTokens` properties, with sensible fallbacks (`0.7` / `800`) when left undefined, across all four provider implementations.

### 3.3 — Temperature comparison tests
Ran two side-by-side tests (same prompt, different `TEMPERATURE` values) to demonstrate how a lower temperature produces more deterministic answers and a higher one produces more varied, creative output.

### 3.4 — Sentiment analysis mode
Added a `MODE=SENTIMENT` configuration option that changes the system prompt to rate input text on a 7-point scale (*Very Negative → Very Positive*) and return a structured JSON response:
```json
{ "rating": value, "justification": value }
```

### 3.5 — Android LLM image processing (`GeminiCakeApp`)
Android Studio's "Gemini API Starter" template requires the Canary/preview channel, which wasn't available, so the app was built manually with the same direct-REST-call approach as `AISimpleCalls` (avoiding the deprecated `com.google.ai.client.generativeai` SDK).

The app (`GeminiCakeApp/`) shows three treat photos (cake, cookie, cupcake); the user picks one, enters a prompt, and sends the image + prompt to Gemini 2.5 Flash for a response (recipe, name suggestion, etc.).

**Extra credit — "Mystery Ingredient Game":** a second button asks Gemini to playfully guess one surprising secret ingredient in the selected treat, with a short lighthearted justification, revealed in an animated card below the main answer.

---

## Section 4 — Firebase

### 4.2 — Friendly Chat codelab (`build-android-start`)
Completed the [Firebase Friendly Chat codelab](https://firebase.google.com/codelabs/firebase-android). Firebase project created and registered with package `com.google.firebase.codelab.friendlychat`:

- **Authentication** — Email/Password enabled
- **Realtime Database** — stores chat messages, rules restricted to `auth != null`
- **Cloud Storage** — *not configured* (requires the paid Blaze plan); image-message sending is therefore disabled/expected to fail, text chat and sign-in work fully
- Fixed the known ActionBar overlap bug on the sign-in screen by switching `SignInActivity.kt`'s `onStart()` to `R.style.AppThemeNoActionBar`

### 4.3 — Notes Pro tutorial (`NotesProXMLViews3`)
Completed the Notes App With Firebase tutorial. Firebase project created and registered with package `com.notes.notesproxmlviews`:

- **Authentication** — Email/Password with email verification required before login
- **Cloud Firestore** — stores each user's notes in `notes/{uid}/my_notes`

The provided starter code had Login, Create Account, Splash, and the note detail (save/delete) screens already working, but `MainActivity`'s notes list was an empty skeleton. Completed it by adding:
- `item_note.xml` — note card layout
- `NoteAdapter.kt` — RecyclerView adapter bound to a live Firestore snapshot listener
- `main_menu.xml` — popup menu with a working **Sign Out** action

**Extra credit (optional image per note):** not implemented — would require either Cloud Storage (Blaze billing) or a Base64-in-Firestore workaround; skipped by choice.

---

## General notes

- All source comments were stripped from `build-android-start`, `NotesProXMLViews3`, and `AISimpleCalls` as a cleanup pass.
- API keys are kept out of version control via `local.properties` / `config.properties`, both gitignored.
- Firebase Cloud Storage was intentionally left unconfigured in both Firebase apps to avoid the Blaze (pay-as-you-go) billing requirement — this is a deliberate scope decision, not an oversight.
