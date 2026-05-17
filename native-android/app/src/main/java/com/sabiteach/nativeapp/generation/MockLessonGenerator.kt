package com.sabiteach.nativeapp.generation

import com.sabiteach.nativeapp.model.Lesson
import com.sabiteach.nativeapp.model.SupportExplanation
import java.time.Instant
import java.util.UUID

class MockLessonGenerator : LessonGenerator {
    override val mode: GeneratorMode = GeneratorMode.Mock

    override suspend fun availability(): GeneratorAvailability = GeneratorAvailability.Ready

    override suspend fun generate(request: LessonGenerationRequest): Lesson {
        val normalizedTopic = request.topic.trim().ifEmpty { "Nouns" }
        val isSentenceTopic = normalizedTopic.contains("sentence", ignoreCase = true)

        return if (isSentenceTopic) {
            Lesson(
                id = UUID.randomUUID().toString(),
                lessonTitle = "Building Simple Sentences (SVO)",
                classLevel = request.classLevel,
                subject = request.subject,
                supportLanguage = request.supportLanguage,
                topic = normalizedTopic,
                learningObjective = "Learners will be able to construct clear simple sentences in subject-verb-object order.",
                teacherExplanationEnglish = "A simple sentence can be built with three main parts. The subject tells us who or what acts. The verb shows the action. The object receives the action. Write one example on the board and let the class point out each part.",
                supportExplanation = SupportExplanation(
                    language = request.supportLanguage,
                    text = "Use a short Efik support note to explain subject, verb, and object with one easy example."
                ),
                examples = listOf(
                    "The girl reads a book.",
                    "Mama cooks rice.",
                    "The dog chases the ball."
                ),
                classActivity = "Give pupils subject, verb, and object word cards and ask them to arrange them into correct sentences.",
                quizQuestions = listOf(
                    "What are the three main parts of a simple sentence?",
                    "In 'The boy kicked the ball', which words are the subject?",
                    "In 'Mama cooks rice', which word is the verb?",
                    "In 'The dog chased the ball', which words are the object?",
                    "Write one simple sentence in subject-verb-object order."
                ),
                answerKey = listOf(
                    "Subject, verb, and object.",
                    "The boy.",
                    "cooks.",
                    "the ball.",
                    "Accept any correct simple sentence in subject-verb-object order."
                ),
                takeHomeRevision = "Write five simple sentences about your family using subject, verb, and object order.",
                createdAt = Instant.now().toString()
            )
        } else {
            Lesson(
                id = UUID.randomUUID().toString(),
                lessonTitle = "Understanding Nouns",
                classLevel = request.classLevel,
                subject = request.subject,
                supportLanguage = request.supportLanguage,
                topic = normalizedTopic,
                learningObjective = "Learners will be able to identify nouns as naming words for people, places, animals, and things.",
                teacherExplanationEnglish = "Tell the class that a noun is a naming word. It can name a person, place, animal, or thing. Write a few examples on the board and ask learners to sort them into the right groups.",
                supportExplanation = SupportExplanation(
                    language = request.supportLanguage,
                    text = "Use a short Efik support note to explain that nouns are naming words."
                ),
                examples = listOf(
                    "The teacher entered the classroom.",
                    "The goat ran across the road.",
                    "My book is on the desk."
                ),
                classActivity = "Write ten words on the board and ask pupils to sort them into people, places, animals, and things.",
                quizQuestions = listOf(
                    "What is a noun?",
                    "Which word is the noun in 'The boy kicked the ball'?",
                    "Name one noun that is a place.",
                    "Name one noun that is an animal.",
                    "Write one sentence with a noun."
                ),
                answerKey = listOf(
                    "A noun is a naming word for a person, place, animal, or thing.",
                    "boy and ball are nouns.",
                    "Examples include school, market, or village.",
                    "Examples include goat, dog, or bird.",
                    "Accept any correct sentence with a noun."
                ),
                takeHomeRevision = "Write four nouns at home: one person, one place, one animal, and one thing.",
                createdAt = Instant.now().toString()
            )
        }
    }
}
