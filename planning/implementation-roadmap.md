# Implementation Roadmap

## Immediate Direction

We are done choosing the broad idea.

Now we need to choose the smallest version that is still strong enough to demo.

## Recommended Build Sequence

### Step 1: Lock The First Teaching Slice

Choose:

- one class level
- one support language
- one subject
- five to ten lesson topics

Recommended default:

- Subject: `Primary English`
- Level: `Primary 5`
- Support language: `Efik`

Alternative:

- Subject: `Primary English`
- Level: `Primary 5`
- Support language: `Ibibio`

## Why Primary 5

- old enough for clear grammar and reading topics
- easy for judges to understand
- broad enough to show real classroom utility
- simple enough for a clean MVP

### Step 2: Define The Core Workflow

Version one flow:

1. Teacher selects class and support language.
2. Teacher enters an English topic.
3. Gemma 4 generates structured lesson output.
4. Teacher saves the lesson locally.
5. Teacher reopens the lesson offline.

### Step 3: Create The First Content Packs

We need:

- lesson schema
- English topic list
- local-language classroom phrase pack
- core glossary for the selected language

### Step 4: Scaffold The App

First screens:

- home
- lesson generator
- generated lesson view
- saved lessons
- settings for language and offline packs

### Step 5: Build The Generation Layer

The generation layer should produce:

- lesson objective
- teacher explanation in English
- support explanation in selected language
- examples
- class activity
- five quiz questions
- answer key
- take-home revision

### Step 6: Prove Offline Usefulness

We need to demonstrate:

- local lesson storage
- cached content pack
- usable workflow in airplane mode

### Step 7: Prepare The Demo Narrative

The demo should show:

- teacher problem
- lesson generation
- bilingual teaching support
- offline reopening of saved content

## Build Order

- lock scope
- prepare curriculum slice
- prepare language pack
- define output schema
- scaffold mobile app
- wire generation
- save lessons locally
- test on device
- record demo

## Rule

Do not start building screens until the teaching slice is locked.
