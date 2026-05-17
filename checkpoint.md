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

The native Android app is now a working transition build, not a finished replacement.

What exists already:

- Kotlin lesson models
- Kotlin lesson-mode formatters
- Compose UI for generate -> review -> save
- mock generator
- remote API generator that can call the existing lesson server
- local lesson persistence across app restarts
- verified Gradle build and emulator run path
- real ML Kit Prompt API / AICore integration path for supported devices
- clear generator availability states in the UI

What does not exist yet:

- proven successful on-device lesson generation on a supported physical phone
- prompt/output hardening for real classroom-quality results on device
- physical-device validation of download and warmup behavior

## Verified Machine State

Verified from the shell in this workspace:

- Android Studio exists at `/Applications/Android Studio.app`
- Android SDK folder exists at `/Users/Macintosh/Library/Android/sdk`
- shell Java runtime is not available
- `adb` is not on PATH
- `sdkmanager` is not on PATH

This means the native Android branch should currently be treated as **Android Studio-first**, not terminal-first.

## Immediate Priorities

1. Validate the AICore path on a supported physical Android device.
2. Tune the on-device prompt/output path if the single-call lesson schema proves too large or inconsistent.
3. Keep unsupported-device behavior explicit and graceful.
4. Only then tighten the public offline claim.

## Resume Rule

When resuming native Android work, the first thing to report should be one of:

- the first Gradle sync error
- the first build/run error
- the first device-side runtime error
