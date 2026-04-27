# Gemma Integration Path

## The Important Constraint

Our current mobile app is an Expo prototype.

That is good for:

- UI iteration
- fast device testing
- lesson flow validation

It is not the cleanest final path for native on-device Gemma 4 integration.

## Best Near-Term Path

Use two phases.

### Phase 1: Real Generation Via Small Backend

Keep the current Expo app.

Add a tiny service that:

- receives class, topic, and support language
- prompts Gemma 4
- returns lesson JSON matching our schema

This is the fastest path to replacing the mock generator with real model output.

### Phase 2: Native Android On-Device Integration

Move the generation layer into Android-native code.

This is the stronger hackathon story if we can complete it cleanly.

## Official Android Paths

Google’s official Android story for Gemma 4 now centers on on-device Android support through the ML Kit GenAI Prompt API and the AICore Developer Preview, where you can target Gemma 4 E2B or E4B on supported devices. citeturn2search0turn2search6

Google also points developers to Google AI Edge tooling for local experiences on mobile and edge devices, and MediaPipe’s LLM Inference API is the established Android SDK route for running supported LLMs on-device. citeturn1search0turn3search0turn1search9

## Recommended Decision

Do not block the project on native Android integration today.

Instead:

1. run the Expo prototype on your phone now
2. wire a real generation backend next
3. decide whether to invest in native Android Gemma 4 after the product flow is solid

## Why

- It gets us real outputs faster.
- It keeps the app testable today.
- It preserves the option to move to true on-device generation later.
