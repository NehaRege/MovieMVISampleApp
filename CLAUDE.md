# CLAUDE.md

## Project

Android app (Kotlin, Jetpack Compose) that lists popular movies from the TMDB API and shows a detail screen.

Goals:
- **Production quality**: clear module boundaries, MVI + clean architecture, testable, idiomatic Kotlin.
- **Up to date**: use the latest stable Android, Jetpack and Kotlin technologies. When existing code uses an older pattern, API or library, proactively point it out and suggest the modern alternative with its trade-offs. Check current versions rather than relying on memory.
- **Room to experiment**: this project is also where new libraries, architecture and system-design ideas, and AI-assisted development workflows get tried out, so trade-off discussions and alternative approaches are welcome.

## Commands

```bash
./gradlew assembleDebug                      # build the app
./gradlew installDebug                       # install on a connected device/emulator
./gradlew test                               # all JVM unit tests
./gradlew :data:testDebugUnitTest            # unit tests for one module
./gradlew :module:testDebugUnitTest --tests "<fully.qualified.TestClass>"   # single test class
./gradlew connectedAndroidTest               # instrumented tests (needs device)
./gradlew lint
```

The Gradle daemon runs on JetBrains JDK 21, provisioned via the foojay resolver (`gradle/gradle-daemon-jvm.properties`). Modules compile to Java/JVM target 11.

## Architecture

- **Features**: each feature (`movies` = list, `detail`) is split into three modules:
  - `-presentation`: MVI types and the presenter (an AndroidX `ViewModel`). No Compose.
  - `-ui`: stateless Compose screen taking `state`, `onEvent`, the `coordinator` flow and callbacks.
  - `-navigation`: a `*Route` composable that obtains the presenter via `viewModel(factory)`, collects state, fires the initial `ScreenOpened` event in a `LaunchedEffect`, and wires the screen.
- **`app`**: `MainActivity` → `MoviesRoot` (Scaffold, top bar, bottom nav; bottom bar hidden on `DETAIL`) → `MoviesNavHost`. `SEARCH` and `WATCHLIST` are placeholder destinations.

Any module containing `@Composable` code must apply the `kotlin.compose` plugin, or the Kotlin 2.x compiler fails with an IR lowering `BackendException`.

## Guardrails (always follow)

- Run `./gradlew assembleDebug` after changes, before saying a task is done.
- Git: stage files by name (never `git add .` or `-A`), one logical change per commit, never push or rewrite history unless asked.
- Before every commit, show me the exact commit message (and the files it will include) and wait for my approval. This applies to each commit separately, including when splitting changes into several commits.
- Never commit secrets (API keys, tokens).
- Don't add new libraries or frameworks without asking.
- Many files contain the author's study-note comments, plus markdown notes such as `domain/.../usecase/doc_usecase.md`. Never change, condense or remove them without my approval. If a note is unclear, outdated or could be worded better, suggest the improvement and wait for my OK.

## Current conventions (defaults, not dogma)

Follow these for consistency. If you see an approach that would be better — one that scales further, is easier to test, maintain or change, or reflects current Android best practice — say so: explain the trade-offs and long-term costs, and wait for my decision before deviating. Never silently change a pattern.

- Module boundaries: features depend only on `domain`, `core` and their own feature modules, never on `:data` or `:app`; only `:app` wires implementations to interfaces; no feature-to-feature dependencies; `domain` has no Android/framework imports.
- New feature = three modules (`-presentation`, `-ui`, `-navigation`), registered in `settings.gradle.kts` and wired in `MoviesGraph` and `MoviesNavHost`.
- MVI per feature: immutable `XState` exposed as `StateFlow` (private `MutableStateFlow`); sealed `XEvent`; non-suspend `presenter.onEvent()` launching in `viewModelScope`; one-off effects via an `XCoordinator` `SharedFlow`, never in state.
- Manual DI in `app/.../MoviesGraph.kt` (a DI framework is planned; don't migrate without asking).
- Kotlin only; dependency versions only in `gradle/libs.versions.toml`.
- Naming: `XPresenter`, `XState`, `XEvent`, `XCoordinator`, `XRoute`, `XScreen`.
- Commit messages: Conventional Commits with a scope, e.g. `feat(navigation): ...`, `build(deps): ...`, `chore(gitignore): ...`, `docs(notes): ...`.
  - Subject line: max 72 characters including the `type(scope): ` prefix, aim for ~50; imperative mood, lowercase after the prefix, no trailing period.
  - Body (optional): blank line after the subject, wrapped at 72 characters, explaining *why* rather than *what*.
