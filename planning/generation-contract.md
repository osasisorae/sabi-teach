# Generation Contract

## Purpose

Define exactly what Gemma 4 is expected to generate in version one.

## Core Rule

Gemma 4 must return structured lesson content, not an open-ended chat response.

## Input Contract

The generation module receives:

- class level
- subject
- support language
- topic
- optional curriculum notes
- optional glossary entries
- optional classroom phrases

## Output Contract

Gemma 4 must generate:

- lesson title
- lesson objective
- teacher explanation in English
- support explanation in Efik
- examples
- one classroom activity
- five quiz questions
- answer key
- take-home revision note

## Style Rules

- explanations must be short and teacher-friendly
- quiz questions must match the topic
- English output must be clear and age-appropriate
- Efik support output must stay concise and instructional
- avoid unsupported claims or invented curriculum standards

## Safety Rules

- do not pretend uncertainty does not exist
- if support-language output is uncertain, keep it simpler
- do not output harmful, insulting, or politically charged classroom content
- avoid culturally alien examples when simpler local examples exist

## Formatting Rules

- every generation should map cleanly to the lesson schema
- arrays should stay short and readable
- no markdown tables
- no unnecessary prose outside the schema fields

## Quality Check

A generation is acceptable if:

- all required fields are present
- the topic is correctly reflected in the lesson
- the quiz matches the lesson explanation
- the answer key is internally consistent
- the Efik support text is brief and readable

## Failure Handling

If generation fails validation:

- reject malformed output
- retry with a stronger schema reminder
- fall back to a minimal safe lesson shell if needed
