# Mobile Quiz - Cat Breeds & Quiz App

An Android app that combines cat breed exploration with an interactive quiz and global leaderboard.

## About

Users can browse cat breeds, take quizzes about them, and compete on a global leaderboard. Breed data is fetched from TheCatAPI.

### Features

- **User registration & profile** — create an account, view and edit profile
- **Cat breed browser** — searchable breed list, details view, and photo gallery
- **Quiz** — dynamic quiz with a 5-minute countdown, multiple question types, and score tracking
- **Leaderboard** — global rankings and score comparison

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin 1.9.23 |
| UI | Jetpack Compose + Material Design 3 |
| Architecture | MVVM |
| DI | Hilt 2.51 |
| Database | Room 2.6.1 |
| Networking | Retrofit 2.11.0 + OkHttp 4.12.0 |
| Auth | Firebase Auth + Google Sign-In |
| Images | Coil 2.6.0 |
| Navigation | Jetpack Navigation 2.7.7 |

- Min SDK: Android 26 (Android 8.0)
- Target SDK: Android 34 (Android 14)

## Running the Project

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or newer
- JDK 17
- Android SDK 34
- A device or emulator running Android 8.0+

### Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/ognjenkojic13/Mobile-Quiz.git
   cd Mobile-Quiz
   ```

2. **Open in Android Studio**
   - Go to `File > Open` and select the cloned folder
   - Wait for Gradle to sync dependencies

3. **Run**
   - Connect an Android device or start an emulator
   - Click the green `Run` button or press `Shift+F10`

### Gradle (command line)

```bash
# Build and install debug
./gradlew installDebug

# Prod build
./gradlew assembleProd

# Stage build
./gradlew assembleStage

# Run tests
./gradlew test
```

## Project Structure

```
app/src/main/kotlin/raf/rs/rma/
├── navigation/          # Navigation and bottom bar
├── breeds/              # Cat breeds feature (API, list, details, gallery)
├── quiz/                # Quiz, results, and leaderboard
├── users/               # User registration
├── account/             # Account management
├── networking/          # Retrofit configuration
├── db/                  # Room database
└── ui/                  # Theme and styling
```

## API

Breed data is provided by [TheCatAPI](https://thecatapi.com).

## Build Variants

| Variant | Description |
|---|---|
| `debug` | Development build with logging |
| `stage` | Staging environment |
| `prod` | Production build |
