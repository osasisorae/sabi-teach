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

## Current Scaffold State

What exists in `native-android/` right now:

- Kotlin lesson models
- Kotlin lesson-mode formatters
- Compose UI for generate -> review -> save
- mock generator
- remote API generator
- AICore generator stub

What is still missing:

- verified Gradle sync/build
- persistent local lesson storage
- reopen-saved-lessons flow after restart
- real AICore or ML Kit prompt execution

## Verified Local Machine State

Verified from the shell:

- Android Studio is installed at `/Applications/Android Studio.app`
- Android SDK directory exists at `/Users/Macintosh/Library/Android/sdk`
- no shell Java runtime is available
- `adb` is not on PATH
- `sdkmanager` is not on PATH

So this phase is still a scaffold and environment-validation pass, not a proven native build yet.

## Immediate Build Plan

1. Open `native-android/` in Android Studio.
2. Let Gradle sync run with the embedded JDK.
3. Report and fix the first sync/build error.
4. Add local persistence so saved lessons survive restart.
5. Validate the remote API generator on a physical Android device.
6. Replace the AICore stub with a real on-device runtime on supported hardware.

## Non-Goals Right Now

- iOS
- cloud inference
- team features
- curriculum breadth
- polished analytics
- teacher accounts
