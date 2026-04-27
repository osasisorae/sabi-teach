# App Architecture Plan

## Working App Name

SabiTeach

## Locked First Slice

- Class: `Primary 5`
- Subject: `English`
- Support language: `Efik`

## Core User Journey

1. Teacher opens app.
2. Teacher selects class and support language.
3. Teacher enters topic.
4. App generates lesson materials with Gemma 4.
5. Teacher reviews the lesson.
6. Teacher saves the lesson locally.
7. Teacher reopens the lesson offline.

## Initial Screen List

- `HomeScreen`
- `GenerateLessonScreen`
- `LessonResultScreen`
- `SavedLessonsScreen`
- `LessonDetailScreen`
- `SettingsScreen`

## Screen Responsibilities

### HomeScreen

- show product purpose
- quick access to generate new lesson
- quick access to saved lessons
- show offline-ready state

### GenerateLessonScreen

- choose class
- choose subject
- choose support language
- enter topic
- trigger generation

### LessonResultScreen

- render generated output in fixed sections
- allow teacher to save lesson
- allow regenerate if needed

### SavedLessonsScreen

- list previously saved lessons
- filter by topic or date

### LessonDetailScreen

- open one saved lesson
- read content offline

### SettingsScreen

- show selected support language
- show available content packs
- show offline status

## Suggested Folder Structure

- `app/`
- `src/components/`
- `src/screens/`
- `src/features/lesson-generation/`
- `src/features/saved-lessons/`
- `src/features/content-packs/`
- `src/lib/storage/`
- `src/lib/generation/`
- `src/lib/types/`
- `content/`
- `schemas/`

## Core Modules

### Lesson Generation Module

Responsibilities:

- build structured prompt input
- invoke Gemma 4 generation path
- validate output against lesson schema
- normalize missing or malformed fields

### Content Pack Module

Responsibilities:

- load local topic packs
- load language support packs
- provide glossary entries and classroom phrases

### Saved Lesson Module

Responsibilities:

- save generated lessons locally
- list saved lessons
- retrieve lesson by id

### Settings Module

Responsibilities:

- persist selected class defaults
- persist selected support language
- expose offline readiness state

## State Shape

The app only needs a small state model:

- current class level
- current subject
- current support language
- current topic input
- current generated lesson
- saved lessons list

## Data Flow

1. Teacher selects generation inputs.
2. App loads relevant curriculum and language pack context.
3. App sends structured request to generation module.
4. Generation module returns lesson JSON.
5. UI renders the lesson from the schema.
6. Teacher saves locally.

## Offline Behavior

Offline readiness means:

- topic packs exist on device
- language pack exists on device
- saved lessons exist on device
- app can reopen previous content without network

## Version One Non-Requirements

- user auth
- cloud sync
- teacher accounts
- analytics
- admin dashboard

## Architecture Rule

The UI must render from structured lesson data, not free-form text blobs.
