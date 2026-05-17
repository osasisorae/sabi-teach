# Checkpoint

## Current Product Reality

SabiTeach is currently strongest as an **Expo prototype plus a local generation server**.

What is working today:

- teacher enters a topic and generates a structured lesson
- lesson output follows a fixed schema
- saved lessons are stored on the phone in the Expo app
- saved lessons reopen offline in the Expo app
- the server can generate lessons through either `mock` mode or local Ollama

What is in active progress:

- richer lesson presentation modes inside the Expo app:
  - teacher mode
  - student handout mode
  - quick oral quiz mode
  - board work mode
- a native Android rewrite path under `native-android/`

## Important Truth

The project is **not yet fully offline for new lesson generation**.

Current real generation path is:

- phone -> local server -> Ollama on laptop

The offline claim we can safely make today is narrower:

- previously saved lessons reopen on the phone without internet

## Native Android Status

The native Android app is now a scaffold, not a finished replacement.

What exists already:

- Kotlin lesson models
- Kotlin lesson-mode formatters
- Compose UI for generate -> review -> save
- mock generator
- remote API generator that can call the existing lesson server
- AICore generator stub for future on-device integration

What does not exist yet:

- verified Gradle sync and run path
- persistent local lesson storage
- real on-device prompt execution
- device capability handling for supported vs unsupported Android phones

## Verified Machine State

Verified from the shell in this workspace:

- Android Studio exists at `/Applications/Android Studio.app`
- Android SDK folder exists at `/Users/Macintosh/Library/Android/sdk`
- shell Java runtime is not available
- `adb` is not on PATH
- `sdkmanager` is not on PATH

This means the native Android branch should currently be treated as **Android Studio-first**, not terminal-first.

## Immediate Priorities

1. Finish and commit the upgraded Expo teaching modes so the proven workflow is clean.
2. Open `native-android/` in Android Studio and fix the first Gradle sync or build failure.
3. Add real local persistence to native Android so saved lessons survive app restarts.
4. Replace the AICore stub with a real supported on-device generation path, or expose a clear unavailable state on unsupported devices.

## Resume Rule

When resuming native Android work, the first thing to report should be one of:

- the first Gradle sync error
- the first build/run error
- the first device-side runtime error
