# SabiTeach Mobile

Expo / React Native scaffold for the first SabiTeach teacher workflow.

## Current Scope

- Class: `Primary 5`
- Subject: `English`
- Support language: `Efik`
- Flow: generate lesson -> review -> save locally -> reopen offline

## What Is Real Today

- Android-first Expo app scaffold
- starter topic chips for Primary 5 English
- fixed structured lesson rendering
- switchable generation module
- AsyncStorage-based saved lessons

## What Is Not Real Yet

- on-device Gemma 4 inference inside the Expo app
- validated Efik output
- OCR or textbook capture
- full curriculum packs

## Run

Install dependencies first, then:

```bash
npm install
npm run start
```

For Android:

```bash
npm run android
```

## Implementation Notes

- `App.tsx` contains the first narrow teacher flow
- `src/lib/generation.ts` switches between `mock` and `remote` modes
- `src/lib/mock-generator.ts` is the placeholder generation layer
- `src/lib/storage.ts` saves lessons locally
- `src/content/` contains the initial content seeds used by the scaffold

## Generation Modes

Default mode is `mock`.

To point the app at a real generation backend, create `.env` in this folder with:

```bash
EXPO_PUBLIC_SABITEACH_GENERATION_MODE=remote
EXPO_PUBLIC_SABITEACH_API_BASE_URL=http://YOUR_COMPUTER_IP:4000
```

Then restart Expo.

The app expects:

- `POST /generate-lesson`
- JSON body:

```json
{
  "classLevel": "Primary 5",
  "subject": "English",
  "supportLanguage": "Efik",
  "topic": "Nouns"
}
```

- JSON response:

```json
{
  "lesson": {
    "...": "must match the lesson schema"
  }
}
```

## Recommended Next Build Step

Keep Expo for the UI and add a small generation server first.

That lets us prove real Gemma 4 output quickly before moving to a native Android on-device integration.
