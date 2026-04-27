# SabiTeach

Offline teacher copilot for low-connectivity schools in Nigeria.

## Why This Exists

This project is our entry for the Gemma 4 Good Hackathon.

We are not building a general edtech platform.
We are building one sharp product:

- help a teacher prepare and teach better with little or no internet

## Core Product Thesis

A teacher should be able to use one Android phone to:

- choose a class and subject
- enter a lesson topic or snap a page
- generate a simple lesson plan
- generate a plain-language explanation
- generate a short quiz and marking guide
- generate a take-home revision sheet

The system should remain useful offline after the needed content pack and model are installed.

## Initial Subject Direction

We will start with:

- Subject: English
- Core use case: teach English using local-language support
- Language strategy: English output plus selected Nigerian language scaffolding

This is the sharpest first wedge because it helps students access every other subject later.
We will design the app so additional language packs and subjects can be added without changing the core workflow.

## Target Tracks

- Main Track
- Impact Track: Future of Education
- Special Technology Track: LiteRT

## Project Rules For Ourselves

- Build for one teacher, one classroom, one phone
- Start with one subject and one curriculum pack
- Start with a small number of supported languages, not all languages
- Prefer offline-first over feature breadth
- Prefer credibility over flashy claims
- Keep Gemma 4 central, visible, and defensible

## File Map

- `context/overview.md`: hackathon overview and competition context
- `planning/product-concept.md`: product vision and user story
- `planning/goal-guardrails.md`: what we will and will not build
- `planning/mvp-scope.md`: exact MVP scope
- `planning/architecture-direction.md`: technical direction
- `planning/app-stack-decision.md`: chosen mobile stack and rationale
- `planning/app-architecture-plan.md`: concrete app modules and screen flow
- `planning/implementation-roadmap.md`: execution sequence
- `planning/first-teaching-slice.md`: exact first classroom slice
- `planning/language-strategy.md`: multilingual scope and language-pack plan
- `planning/pitch-and-story.md`: writeup and video story
- `planning/generation-contract.md`: fixed generation rules for Gemma 4
- `planning/submission-checklist.md`: required submission assets
- `schemas/lesson-output-schema.json`: structured lesson output contract
- `content/primary5-english/topics.json`: starter topic pack
- `content/language-packs/efik/classroom-phrases.json`: starter Efik support phrases
- `content/language-packs/efik/glossary.json`: starter Efik glossary

## Working Standard

Every product, design, and engineering decision should answer this question:

Does this make a low-connectivity Nigerian classroom meaningfully better for a real teacher in the next few weeks?

If the answer is no, it does not belong in the MVP.

## Locked Default

Unless we explicitly change it later, the working default is:

- Class: `Primary 5`
- Subject: `English`
- Support language: `Efik`
- Device target: `Android`
- App stack: `Expo / React Native`
