# SabiTeach Native Android

This folder is the transition path toward a truly on-device Android version of SabiTeach.

The current Expo prototype already proved:

- the teacher workflow
- the lesson schema
- the teaching modes
- local lesson saving

What it does not prove yet is full on-device lesson generation on the phone.

This native Android scaffold exists to fix that.

## Current State

What is here now:

- Kotlin lesson models
- Kotlin teaching-mode formatters
- a native generation boundary
- a mock native generator for development
- a remote API generator that can call the existing lesson server
- an AICore generator stub where the real ML Kit Prompt API integration will live

What is not here yet:

- verified Gradle setup
- Room storage
- real AICore prompt execution on device

## Why The Structure Is Narrow

We are not rebuilding an edtech platform.

We are rebuilding one sharp workflow:

1. Teacher picks a topic
2. App generates one classroom-ready lesson
3. Teacher switches between teaching views
4. Teacher saves the lesson
5. Saved lessons reopen offline

## Folder Layout

- `app/src/main/java/com/sabiteach/nativeapp/model`
- `app/src/main/java/com/sabiteach/nativeapp/generation`
- `app/src/main/java/com/sabiteach/nativeapp/ui`

## Next Machine Setup

Before this native branch can be built locally on this Mac, install:

- Android Studio
- Java 17+
- Android SDK platform + build tools
- at least one physical Android device for testing

Do not depend on an emulator for the on-device AI path.

## How To Run It Right Now

Today there are three native generation paths:

1. mock generator inside the app
2. remote API generator hitting the existing laptop server
3. future on-device AICore path

The current app will use the remote API generator when `SABITEACH_API_BASE_URL` is provided at build time. Otherwise it falls back to the mock generator.

### Run The Server

From `server/`:

```bash
PORT=4000 SABITEACH_PROVIDER=mock node index.mjs
```

For real local model generation through Ollama:

```bash
PORT=4000 SABITEACH_PROVIDER=ollama OLLAMA_BASE_URL=http://127.0.0.1:11434 OLLAMA_MODEL=gemma4:e4b node index.mjs
```

If you want to test from another device on the same LAN, use your Mac's LAN IP and keep port `4000` open locally.

### Point Native Android At The Server

Set one Gradle property before syncing:

```bash
export SABITEACH_API_BASE_URL=http://YOUR_MAC_LAN_IP:4000
```

Or add this to `~/.gradle/gradle.properties`:

```properties
sabiteachApiBaseUrl=http://YOUR_MAC_LAN_IP:4000
```

The app allows cleartext HTTP because this local server is currently expected to run on your laptop over LAN during development.

### Run In Android Studio

1. Open `native-android/` in Android Studio.
2. Set the Gradle JDK to the Android Studio embedded JDK.
3. Let Android Studio install any missing SDK pieces.
4. Connect a physical Android phone with USB debugging enabled.
5. Pick the device and run the `app` configuration.

If you do not set an API base URL first, the app still runs, but generation stays on the built-in mock path.

## Native Build Priorities

1. Create the Android Studio project in this folder.
2. Wire the model and teaching-mode files already added here.
3. Replace the mock generator with the real AICore implementation.
4. Add local lesson persistence.
5. Test on a supported physical Android device.
