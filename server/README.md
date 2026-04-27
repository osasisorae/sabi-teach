# SabiTeach Server

Small lesson-generation server for the mobile app.

## What It Does

- exposes `POST /generate-lesson`
- exposes `GET /health`
- returns lesson JSON that matches the mobile app contract

## Why This Exists

This server replaces the mobile app's mock generator without forcing a native Android rebuild first.

## Providers

Set `SABITEACH_PROVIDER` in your environment:

- `mock`
- `ollama`
- `openai`

## Endpoints

### `GET /health`

Returns:

```json
{
  "ok": true,
  "provider": "mock",
  "port": 4000
}
```

### `POST /generate-lesson`

Request body:

```json
{
  "classLevel": "Primary 5",
  "subject": "English",
  "supportLanguage": "Efik",
  "topic": "Nouns"
}
```

Response:

```json
{
  "lesson": {
    "...": "lesson fields"
  }
}
```

## Run

From this folder:

```bash
/Users/Macintosh/LearningHub/gemma-4-good-hackathon/server
```

Start with:

```bash
node index.mjs
```

## Environment

Copy `.env.example` values into your shell or your preferred env loader.

Examples:

### Mock

```bash
PORT=4000 SABITEACH_PROVIDER=mock node index.mjs
```

### Ollama

```bash
PORT=4000 SABITEACH_PROVIDER=ollama OLLAMA_BASE_URL=http://127.0.0.1:11434 OLLAMA_MODEL=gemma4 node index.mjs
```

### OpenAI-Compatible

```bash
PORT=4000 SABITEACH_PROVIDER=openai OPENAI_BASE_URL=http://127.0.0.1:1234/v1 OPENAI_API_KEY=dummy OPENAI_MODEL=gemma4 node index.mjs
```

## Mobile App Pairing

In the mobile folder, create `.env` with:

```bash
EXPO_PUBLIC_SABITEACH_GENERATION_MODE=remote
EXPO_PUBLIC_SABITEACH_API_BASE_URL=http://YOUR_COMPUTER_IP:4000
```

Then restart Expo.

## Note

If the model returns malformed JSON, the server falls back to a minimal safe lesson so the app still works while you tune prompts.
