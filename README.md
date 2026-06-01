# Cloned Stack Overflow

A Stack Overflow clone Android application built with **Jetpack Compose**. The app fetches questions and answers from the [Stack Exchange API](https://api.stackexchange.com/), displays them in a modern UI, and caches data locally for offline access.

## 🛠️ Tech Stack

- **[Hilt](https://dagger.dev/hilt/)** — Dependency injection framework for Android, simplifying DI setup across the app.
- **[Room](https://developer.android.com/training/data-storage/room)** — Local database library for caching API responses and enabling offline support.
- **[Retrofit](https://square.github.io/retrofit/)** — Type-safe HTTP client used to communicate with the Stack Exchange REST API.
- **[Coil](https://coil-kt.github.io/coil/)** — Image loading library optimised for Kotlin and Jetpack Compose, used for loading user avatars and images.
- **[Markwon](https://noties.io/Markwon/)** — Markdown rendering library used to display formatted question and answer content.