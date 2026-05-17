# Implementation Roadmap

## Current Position

The early product-definition work is already complete.

Already completed:

- first teaching slice is locked to `Primary 5 English`
- support language is locked to `Efik`
- lesson schema exists
- starter content packs exist
- Expo mobile app exists
- local lesson saving exists in Expo
- generation server exists with `mock` and Ollama-backed paths

The current roadmap is about **hardening what works** and **moving the product toward native Android on-device generation**.

## Completed Foundation

- product slice selection
- schema definition
- language pack seeding
- Expo prototype scaffold
- remote generation bridge
- offline reopen of saved lessons in Expo

## Current Build Sequence

### Step 1: Finish Expo Workflow Polish

Current open work:

- commit the lesson workbench changes
- keep the app messaging truthful about offline limits
- preserve the narrow teacher workflow that already demos well

### Step 2: Verify Native Android Build

This is now completed at the basic level:

- native Android builds with Gradle
- emulator run path is verified
- generator availability states are rendered in the UI

### Step 3: Reach Feature Parity In Native

Native Android now has:

- local lesson persistence
- reopened saved lessons after app restart
- the same teaching modes already proved in Expo
- clearer generator availability and fallback messaging

The remaining gap in parity is the supported-device on-device path itself.

### Step 4: Replace Scaffolded On-Device Generation

The AICore path is now wired to ML Kit Prompt API, but it still must be proven on supported hardware.

Before claiming true offline generation, we must have:

- a working on-device runtime on supported hardware
- structured JSON output parsed into the lesson model
- clear unsupported-device behavior

### Step 5: Keep Demo Claims Tight

The public story should stay truthful:

- Expo proves the workflow
- native Android is the path to real on-device generation
- saved lessons reopen offline today
- new lesson generation is not yet fully on-device

## Build Order

- finish Expo polish
- verify native Android sync/build
- add native persistence
- validate remote API mode on a phone
- validate real on-device generation on supported hardware
- test on supported Android hardware
- update demo assets and writeup

## Rule

Do not claim full offline generation until the native on-device path actually works on a real device.
