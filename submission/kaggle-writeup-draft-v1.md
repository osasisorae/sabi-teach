# SabiTeach

## An offline-first teacher copilot for English learning in Nigerian classrooms

### Track

Future of Education

### Summary

I’m building SabiTeach because I would genuinely like to see something like this exist in the real world.

It is a simple idea: help a teacher turn one lesson topic into something usable in class, even with weak internet or no internet at all.

The first version is intentionally small:

- Primary 5
- English
- Efik support language

The teacher enters a topic like `Nouns` or `Pronouns`, and the app generates:

- a lesson objective
- a simple teacher explanation in English
- a support explanation in a local language
- examples
- a class activity
- five quiz questions
- an answer key
- a short take-home revision note

### Why I Care About This

I’ve wanted to build something in education for a long time.

The problem is obvious: a lot of teachers are working with too little time, too few materials, and unreliable connectivity. At the same time, most AI tools are built for the exact opposite environment. They assume stable internet, personal laptops, subscriptions, and one learner sitting alone with a screen.

That is not the reality for a lot of schools here.

Also, if I’m being honest, education is one of those areas people talk about a lot and invest in far less than they should. I know that. I’m not building this because I think there is some obvious big business model waiting for me. I’m building it because I think it would be useful, and I’d like to see it exist.

### What SabiTeach Is

SabiTeach is an offline-first mobile teaching assistant for low-connectivity classrooms in Nigeria.

I’m not trying to build a full edtech platform here. I’m not trying to build a school operating system. I’m not trying to build a chatbot for everybody.

I’m trying to build one practical teacher workflow:

1. choose class and support language
2. enter a topic
3. generate a structured lesson
4. save it
5. reopen it offline later

That’s it.

### Why English First

I chose English first because it opens the door to every other subject.

But I also don’t want an English-only experience that ignores how people actually learn. A lot of learners understand better when a teacher can bridge from a familiar language into English. That’s why this project is moving in the direction of teaching English with local-language support.

The first support language in this prototype is Efik.

Later, I’d like the same system to support more Nigerian languages carefully, not just claim broad coverage without quality.

### How Gemma 4 Fits In

Gemma 4 is the engine behind the lesson-generation workflow.

The point is not to have a free-form chatbot. The point is to generate a structured classroom output that a real teacher can actually use.

For each lesson, the model is expected to return the same kind of lesson object every time, so the app can render it cleanly and the teacher can review it quickly.

### Current Build

Right now, I have:

- an Android-first mobile prototype
- a structured lesson flow
- local lesson saving for offline reopening
- a switchable generation layer
- a small generation server for connecting the app to real model output

What is still in progress:

- improving the actual generation quality
- validating Efik support text more carefully
- tightening the UI and navigation
- expanding the lesson content packs

So this is real, but still early.

### Why I Think This Matters

I don’t think every good project has to start as a giant plan.

Sometimes it is enough to build one small useful thing properly.

If one teacher with one phone can prepare a little faster, explain a little better, and give students a clearer revision path, that already matters.

That is the spirit of this project.

### Next Step

The next step is straightforward:

- connect the app fully to real Gemma-backed generation
- improve lesson quality topic by topic
- make the experience cleaner on a real phone

SabiTeach is still small right now, but that is on purpose. I’d rather build one honest thing that works than pretend I’m building everything at once.
