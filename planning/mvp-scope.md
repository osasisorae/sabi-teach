# MVP Scope

## MVP Goal

Deliver one working offline-first mobile flow that turns a lesson topic into teacher-ready classroom materials.

## Recommended Starting Scope

- Device target: Android phone
- Audience: teacher
- Subject: English first
- Levels: one or two grade levels max
- Input: typed topic first
- Stretch input: camera capture of textbook page

## Must-Have Features

- class and subject selection
- language selection
- topic input
- generation of a short lesson plan
- generation of a simple explanation in English
- generation of a supporting explanation in a selected local language
- generation of five quiz questions
- generation of a marking guide
- generation of a short take-home revision sheet
- local storage of previous generated lessons
- clearly visible offline mode or offline readiness

## Good Stretch Features

- multilingual language packs
- camera-to-text topic extraction
- difficulty toggle
- export to printable format
- audio readout for generated explanation
- Basic Science lesson generation using the same language-support workflow

## Not In MVP

- parent portal
- student accounts
- payments
- teacher collaboration network
- analytics dashboard
- adaptive learning engine
- multi-school admin tooling
- full curriculum coverage across all subjects

## First Subject Recommendation

Start with `Primary English`.

Why this is the best first subject:

- it is the clearest story for teachers, parents, and judges
- it naturally benefits from bilingual explanation
- it improves access to other subjects later
- it avoids some of the validation complexity of math

Stretch subject after English:

- Basic Science

This is especially compelling if we can explain science concepts in Ibibio, Efik, Yoruba, Hausa, or another selected language.

## Example MVP Flow

1. Teacher opens the app.
2. Teacher selects `Primary 5 English`.
3. Teacher selects a support language such as `Efik`.
4. Teacher enters `Parts of speech`.
4. Gemma 4 generates:
   - 20-minute lesson plan
   - simple explanation in English
   - support explanation in the selected language
   - classroom examples
   - five quiz questions
   - answer key
   - revision handout
5. Teacher saves and reopens the lesson offline.

## Output Format Requirements

Every generated lesson should produce the same structured sections:

- lesson objective
- teacher explanation in English
- local-language support notes
- examples
- class activity
- quiz
- marking guide
- take-home revision

## Quality Bar

The output must be:

- age-appropriate
- easy to skim
- usable by a busy teacher
- consistent in structure
- safe and non-fabricated where claims matter
- respectful of the selected support language

## MVP Success Criteria

We can call the MVP successful if:

- a teacher can complete the main flow in under two minutes
- the app works in airplane mode after installation
- the output is classroom-usable without major rewriting
- the demo is strong enough to explain in one sentence and prove in one minute
