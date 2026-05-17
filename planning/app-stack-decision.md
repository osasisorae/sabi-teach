# App Stack Decision

## Decision

Use a **two-stage app strategy**:

- `Expo / React Native` for the proven prototype
- `Native Android` for the true on-device generation path

## Why

- Expo was the fastest way to prove the teacher workflow.
- Expo already proved local lesson saving and offline reopening.
- Expo does not yet give us a credible end state for on-device AI generation on Android hardware.
- Native Android is the serious path for AICore / ML Kit style on-device execution.

## Target Platform

- Primary target: Android only
- Secondary target: none until the Android path is real

## Current Repo Reality

What is true now:

- the Expo app is the working demo
- the server-backed generation path is working
- native Android is scaffolded but not yet verified end to end

## Immediate Technical Posture

Optimize for:

- strong Android demo flow
- truthful offline claims
- local persistence
- clear generator boundaries
- gradual migration from Expo to native Android where necessary

## Recommended Core Dependencies

- Expo / React Native for prototype speed
- Kotlin + Jetpack Compose for native Android
- AsyncStorage in Expo
- Room or another device-local persistence layer in native Android
- lightweight local content such as JSON packs

## Why Not Overbuild Now

We still do not need a heavy backend to prove the product.

The proof we need is:

- the classroom workflow is real
- Gemma 4 is central
- the app remains useful offline
- the transition to real on-device generation is believable and concrete

## Architecture Rule

Do not let the prototype stack decision force us into weak claims about offline generation.
