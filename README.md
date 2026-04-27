# SabiTeach

Teach English with local-language support.

SabiTeach is a mobile-first teacher copilot for low-connectivity classrooms in Nigeria. It helps a teacher turn one lesson topic into a structured classroom-ready lesson with:

- a learning objective
- a teacher explanation in English
- a short support explanation in a local language
- examples
- a class activity
- quiz questions
- an answer key
- a take-home revision note

## Current Scope

- Class: `Primary 5`
- Subject: `English`
- Support language: `Efik`
- Model path: `Gemma 4 via Ollama`

## What Is Working

- Android-first Expo mobile app
- structured lesson generation flow
- local saved lessons on the phone
- lesson reopening without internet after saving
- generation server that connects the app to real model output

## What This Is Not

- not a full edtech platform
- not a school management system
- not a generic chatbot for everyone

The product is intentionally narrow: one teacher, one phone, one lesson workflow.

## Repo Structure

- [mobile](/Users/Macintosh/LearningHub/gemma-4-good-hackathon/mobile): Expo / React Native app
- [server](/Users/Macintosh/LearningHub/gemma-4-good-hackathon/server): lesson-generation server
- [content](/Users/Macintosh/LearningHub/gemma-4-good-hackathon/content): starter curriculum and language packs
- [branding](/Users/Macintosh/LearningHub/gemma-4-good-hackathon/branding): logo assets
- [submission](/Users/Macintosh/LearningHub/gemma-4-good-hackathon/submission): writeup, script, and submission assets

## Run The App

From [mobile](/Users/Macintosh/LearningHub/gemma-4-good-hackathon/mobile):

```bash
npm install
npm run start
```

## Run The Server

From [server](/Users/Macintosh/LearningHub/gemma-4-good-hackathon/server):

```bash
PORT=4000 SABITEACH_PROVIDER=ollama OLLAMA_BASE_URL=http://127.0.0.1:11434 OLLAMA_MODEL=gemma4:e4b node index.mjs
```

## Pair Mobile With The Server

In [mobile](/Users/Macintosh/LearningHub/gemma-4-good-hackathon/mobile), set:

```bash
EXPO_PUBLIC_SABITEACH_GENERATION_MODE=remote
EXPO_PUBLIC_SABITEACH_API_BASE_URL=http://YOUR_COMPUTER_LAN_IP:4000
```

Then restart Expo.

## Demo Notes

Safe claims for the current build:

- lesson generation runs locally through Gemma 4 via Ollama
- saved lessons reopen offline on the phone

Claims we should avoid unless we build them:

- full on-device phone inference
- broad support for many Nigerian languages
- full curriculum coverage
