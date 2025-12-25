

# 📱 Agent Messaging App 

## Overview

This Android app allows a customer support agent to:

* Log in using provided credentials
* View all customer message threads
* Open a conversation thread
* Send replies to customers
* Refresh messages manually via pull-to-refresh

The app is built using **Jetpack Compose**, follows **MVVM architecture**, and uses **Room + Flow** for reactive local caching.

---

## 🛠 Setup Instructions

### Steps

1. Clone the repository
2. Open the project in Android Studio
3. Sync Gradle
4. Run the app on an emulator or physical device (Android 8+)

No additional configuration is required.
The backend API base URL is already configured.

---

## 🧱 Architecture Overview

The app follows **MVVM + Repository pattern**:

```
UI (Jetpack Compose)
        ↓
ViewModel (StateFlow, business logic)
        ↓
Repository (API + Cache coordination)
        ↓
Room Database (single source of truth)
        ↓
Network API (Retrofit)
```

### Key Components

* **Jetpack Compose** → UI layer
* **ViewModel + StateFlow** → State management
* **Repository** → Data abstraction
* **Room** → Local caching
* **Retrofit** → Network calls
* **Hilt** → Dependency Injection

---

## 🔑 Key Decisions

### 1️⃣ Flow-based Room Caching

Messages are stored in Room and exposed as `Flow<List<Message>>`.

**Why?**

* UI reacts automatically to data changes
* No manual reloads after sending messages
* Offline-safe by default

---

### 2️⃣ Refresh Triggered from ViewModel 

The repository **does not auto-fetch** from the network.

Instead:

* The **ViewModel explicitly triggers refresh**

  * When auth token becomes available (first load)
  * When user performs pull-to-refresh

**Why this decision?**

* Avoids hidden side effects in repositories
* Prevents duplicate or unintended API calls
* Makes refresh timing explicit and predictable


---

### 3️⃣ Token Handling

* Authentication token is stored using **DataStore**
* ViewModels observe token via `Flow`
* UI never receives or passes tokens directly

This keeps authentication concerns out of the UI layer.

---

## ⚖️ Trade-offs Made

### ✔ Chosen

* Reactive Flow-based caching
* Explicit refresh logic in ViewModel
* Clear separation of responsibilities

### ❌ Not Chosen

* Auto-refresh inside repository `onStart`
* Passing auth tokens through UI/navigation
* Complex pagination (not required by assignment)

---

## 🚧 Known Limitations

* No pagination for large message lists
* No retry/backoff strategy implemented
* Timestamp formatting is basic (raw ISO string)

These were intentionally skipped to focus on core architecture and correctness.

---

## 🎥 Demo Video (2–3 minutes)
[Demo Video](https://drive.google.com/file/d/1fmuo2YUML8YpIVBm-eO9zgu6rMFEcVY5/view?usp=drivesdk)



