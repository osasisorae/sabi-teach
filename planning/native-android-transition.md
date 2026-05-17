# Native Android Transition

## Goal

Move SabiTeach from:

- Expo UI
- phone -> laptop -> Ollama generation

to:

- native Android UI
- on-device lesson generation on supported Android hardware
- the same lesson schema and teaching modes we already proved in the Expo prototype

## Why This Path

The current app proves the teacher workflow, but it does not meet the strongest offline claim. Saved lessons reopen offline, but new lesson generation still depends on a laptop on the same network.

The next serious version needs:

- on-device generation
- a narrower Android-first product
- a cleaner split between supported and unsupported devices

## Chosen Runtime Direction

Primary path:

- AICore + ML Kit Prompt API
- target Gemma 4 preview models first
- keep the app architecture ready for Gemini Nano 4-compatible devices later

Fallback path:

- keep saved lessons and bundled starter content available even when generation is unavailable
- if needed later, add a second on-device runtime for broader device support

## Product Scope For Native v1

Keep the same teaching-tool shape:

- Teacher mode
- Student handout mode
- Quick oral quiz mode
- Board work mode
- Short Efik support note

Do not expand to multi-subject or multi-language support yet.

## Technical Shape

Native Android app responsibilities:

- generate a structured lesson from topic + class + support language
- store lessons locally
- reopen saved lessons offline
- render the same lesson in multiple teaching views
- expose clear device capability states

Suggested layers:

- `model`
- `generation`
- `storage`
- `ui`

## Current Native State

What exists in `native-android/` right now:

- Kotlin lesson models
- Kotlin lesson-mode formatters
- Compose UI for generate -> review -> save
- mock generator
- remote API generator
- local lesson persistence
- verified Gradle build path
- real ML Kit Prompt API / AICore integration path
- clear `mock` / `remote` / `on-device unavailable` states in the UI

What is still missing:

- successful on-device lesson generation on supported physical hardware
- prompt/output tuning for reliable structured lesson JSON
- validation of model download/warmup on a real supported device

## Verified Local Machine State

Verified from the shell:

- Android Studio is installed at `/Applications/Android Studio.app`
- Android SDK directory exists at `/Users/Macintosh/Library/Android/sdk`
- no shell Java runtime is available
- `adb` is not on PATH
- `sdkmanager` is not on PATH

So this phase is no longer just a scaffold. It is a buildable native app with a real on-device integration path, but not yet a proven supported-device result.

## Immediate Build Plan

1. Open `native-android/` in Android Studio.
2. Let Gradle sync run with the embedded JDK.
3. Run the `aicore` mode on a supported physical Android device.
4. Confirm model availability, download, and warmup behavior.
5. Generate a real lesson end to end and validate the JSON quality.
6. Tighten unsupported-device fallback messaging only after the supported-device path is proven.

## Non-Goals Right Now

- iOS
- cloud inference
- team features
- curriculum breadth
- polished analytics
- teacher accounts
