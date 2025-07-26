# 📱 Exercise Tracker Android App

> ⚠️ **Warning:** The Firebase database used in this project has expired.  
> Therefore, the app cannot be run successfully unless a new Firebase backend is configured.

---

## 1. Android

### 📌 Description

The Android Kotlin project is a mobile application designed to manage workout routines and explore music. With its intuitive interface, users can plan workouts, view nearby training locations, and listen to music while exercising — a practical solution for improving fitness and well-being.

---

### 🧱 Application Architecture

The app includes:

- **Firebase Database**: Stores user authentication data and workouts.
- **API Ninja** ([https://api.api-ninjas.com](https://api.api-ninjas.com)): Provides exercise info based on user input.

![Figure 1. Firebase Database](App_images/firebase_database.png)

---

### 📂 Key Activities

- `LoginActivity.kt` – Handles login/registration.
- `MainActivity.kt` – Hosts all fragments and app navigation.
- `MusicListActivity.kt` – Displays and manages music playlists.

---

### 🧩 Core Fragments

- `AddExercisesFragment.kt` – Exercise search, selection, and duration tracking.
- `MapFragment.kt` – Interactive Google Map with gym markers.
- `MusicCategoryFragment.kt` – Shows music categories.
- `HomeFragment.kt` – Lists today's exercises with details and progress.

---

## 📲 Application Screens

### 🔐 1. Login Page

User login and registration via Firebase.

![Figure 2. Login Page](App_images/login_page.png)

---

### 🏠 2. Home Page (`HomeFragment`)

Displays scheduled exercises for the day using `fetchTodayExercises()`.

![Figure 3. Home Page](App_images/home_page.png)

---

### 📚 3. App Drawer Menu

Navigation menu for Home, Add Exercises, Music, and Map.

![Figure 4. App Drawer Menu](App_images/app_drawer_menu.png)

---

### ➕ 4. Add Exercises

Search and add exercises using external API.

![Figure 5. Add Exercises](App_images/add_exercise.png)

---

### 🗺️ 5. Map View

Interactive map for locating gyms.

![Figure 6. Map View 1](App_images/gym_map_view1.png)  
![Figure 7. Map View 2](App_images/gym_map_view2.png)

---

### 🎵 6. Music Categories

Browse available music categories.

![Figure 8. Music Categories](App_images/music_categories.png)

---

### 🎧 7. Music Playlist

Category-specific music list with player controls.

![Figure 9. Music Playlist](App_images/music_list.png)

---

### 📊 8. Daily Progress Widget

Displays today's exercise progress on the home screen.

![Figure 10. Progress Widget](App_images/widget_daily_progress.png)

---

### 🔔 9. Notification

Shows a persistent notification while the app runs in the background.

![Figure 11. Notification](App_images/app_notification.png)

---

## 🧠 Core Logic Overview

### `MainActivity.kt`

- Sets up UI and navigation
- Handles drawer email
- Manages permission requests
- Launches `NotificationService`

---

### `ExerciseService.kt`

- Sends user queries to the external API
- Parses and returns exercises

---

### `FirebaseService.kt`

- Authenticates users
- Adds and fetches exercises
- Pulls today’s exercise list for Home and Widget

---

### `MusicPlayer.kt`

- Centralized `MediaPlayer` wrapper
- Play, pause, seek, next/previous
- Time tracking and UI sync

---

## ⚙️ Technologies Used

- **Kotlin** – Main language  
- **Firebase** – User authentication and data storage  
- **API Ninjas** – Exercise data  
- **Google Maps SDK** – Map integration  
- **MediaPlayer** – Music playback  
- **Android Studio** – Development environment  

---

> ⚠️ Reminder: Firebase has expired. You must configure a new backend to run the app.
