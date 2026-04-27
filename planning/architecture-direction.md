# Architecture Direction

## Product Shape

Offline-first Android application with local content packs and Gemma 4 at the center of generation.

## Target Technical Story

We want the technical story to be easy for judges to understand:

- Gemma 4 runs locally or in a local-first inference path
- the app uses curriculum-grounded retrieval from packaged content
- generation is structured for teacher workflows
- outputs remain available offline

## Core Components

- Mobile app UI
- Local lesson history store
- Curriculum content pack
- Language support pack
- Prompt and output templates
- Gemma 4 inference layer
- Optional OCR layer for textbook capture

## Proposed MVP Stack

- Frontend: Android-first mobile app
- Local storage: SQLite or other device-local persistence
- Inference: Gemma 4 edge-friendly variant
- Runtime target: LiteRT-oriented deployment path
- Retrieval: lightweight local JSON or SQLite curriculum pack

## Gemma 4 Role

Gemma 4 should do the actual high-value work:

- convert topic input into structured teaching materials
- rewrite content for simpler reading level
- generate examples and quizzes
- generate local-language support explanations

Gemma 4 should not be reduced to a generic chat box.

## Grounding Strategy

To avoid generic or misleading educational output:

- constrain generation with a fixed lesson schema
- retrieve from local curriculum notes or approved content pack
- instruct the model to stay within subject and grade level
- show the teacher the exact grade and topic context used
- keep local-language output focused on explanation support, not open-ended translation of everything

## Offline Strategy

Offline-first means:

- core generation works without live internet
- previously generated lessons are saved locally
- curriculum pack is bundled or downloadable ahead of time
- app remains useful in airplane mode

## MVP Technical Risks

- model size and device performance
- output latency on lower-end Android devices
- OCR quality from low-quality camera images
- local-language quality and terminology consistency

## Risk Mitigation

- start with typed topic input
- start with one subject and one level band
- start with a small number of support languages
- use structured prompts with fixed sections
- keep the first model path simple and local
- treat camera capture as stretch, not foundation

## Demo Architecture Narrative

For the demo and writeup, our architecture should be explainable in one short paragraph:

SabiTeach packages curriculum context and runs a Gemma 4-powered lesson generation workflow on-device or through a local-first edge path, allowing a teacher to create classroom-ready materials and reopen them offline with no dependence on continuous connectivity.
