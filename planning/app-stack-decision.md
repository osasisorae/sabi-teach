# App Stack Decision

## Decision

Use `Expo / React Native` for the first mobile build.

## Why

- We already have an Expo-based mobile project pattern in this workspace: `ifendo-mvp`.
- It is the fastest route to an Android-first prototype.
- It supports a single codebase for future extension.
- It is good enough for a strong hackathon demo.

## Target Platform

- Primary target: Android
- Secondary target: iOS later if useful

## Immediate Technical Posture

Version one should optimize for:

- fast iteration
- local storage
- strong demo flow
- clear offline behavior

It should not optimize for:

- large-scale backend complexity
- multi-device synchronization
- production-grade cloud architecture

## Recommended Core Dependencies

- Expo / React Native
- TypeScript
- AsyncStorage or SQLite for local persistence
- a lightweight local content format such as JSON

## Why Not Overbuild Now

We do not need a complex backend to prove the product.

The hackathon proof is:

- the classroom workflow is real
- Gemma 4 is central
- the app remains useful offline

## Expected App Shape

- mobile client first
- content packs bundled locally
- lessons saved locally
- model integration isolated behind one generation module

## Reuse Opportunity

We can borrow project structure ideas from `ifendo-mvp`, but we should not inherit unrelated feature complexity.

## Constraint

If a stack choice slows down the first Android demo, it is the wrong choice for now.
