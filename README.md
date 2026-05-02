# Grocery Deals – JavaAi Android App

An Android application for managing grocery deals and shopping lists, with an AI-powered assistant.

## Project Structure

```
0353 Project/
├── JavaAi/              # Android Studio project
│   ├── app/
│   │   └── src/main/
│   │       ├── java/com/example/javaai/   # Java/Kotlin source files
│   │       └── res/                        # Resources (layouts, drawables, etc.)
│   ├── build.gradle.kts
│   └── settings.gradle.kts
└── screenshorts/        # App screenshots
```

## Features

- **User Authentication** – Login, signup, and forgot-password flows with OTP verification
- **Shopping Lists** – Create and manage grocery lists
- **AI Assistant** – In-app AI chat fragment for smart suggestions
- **Firebase Integration** – Backend powered by Firebase (Auth, Firestore)
- **Animated Splash Screen** – Logo animation on app launch

## Requirements

- Android Studio Hedgehog or later
- Android SDK 26+ (minSdk 26, targetSdk 35)
- Google `google-services.json` already included in `app/`

## Getting Started

1. Clone this repository.
2. Open `0353 Project/JavaAi` in Android Studio.
3. Let Gradle sync the project dependencies.
4. Run the app on an emulator or physical device (API 26+).

## Screenshots

Screenshots of the app are available in the [`0353 Project/screenshorts/`](0353%20Project/screenshorts/) directory.

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java & Kotlin |
| UI | XML Layouts, Material Design |
| Auth | Firebase Authentication |
| Database | Firebase Firestore |
| AI | Gemini / OpenAI integration |
| Ads | Google AdMob |
| Build | Gradle (Kotlin DSL) |
