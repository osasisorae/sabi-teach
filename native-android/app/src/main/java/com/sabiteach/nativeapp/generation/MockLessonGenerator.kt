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
        val topicKey = normalizedTopic.lowercase()

        return when {
            topicKey.contains("sentence") -> sentenceLesson(request, normalizedTopic)
            topicKey.contains("pronoun") -> pronounLesson(request, normalizedTopic)
            topicKey.contains("verb") -> verbLesson(request, normalizedTopic)
            topicKey.contains("adjective") -> adjectiveLesson(request, normalizedTopic)
            topicKey.contains("tense") -> tenseLesson(request, normalizedTopic)
            topicKey.contains("parts of speech") -> partsOfSpeechLesson(request, normalizedTopic)
            else -> nounLesson(request, normalizedTopic)
        }
    }

    private fun nounLesson(request: LessonGenerationRequest, topic: String): Lesson {
        return buildLesson(
            request = request,
            topic = topic,
            lessonTitle = "Understanding Nouns",
            learningObjective = "Learners will be able to identify nouns as naming words for people, places, animals, and things.",
            teacherExplanationEnglish = "Tell the class that a noun is a naming word. It can name a person, place, animal, or thing. Write a few examples on the board and ask learners to sort them into the right groups.",
            supportText = "Use a short Efik support note to explain that nouns are naming words.",
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
            takeHomeRevision = "Write four nouns at home: one person, one place, one animal, and one thing."
        )
    }

    private fun pronounLesson(request: LessonGenerationRequest, topic: String): Lesson {
        return buildLesson(
            request = request,
            topic = topic,
            lessonTitle = "Understanding Pronouns",
            learningObjective = "Learners will be able to identify pronouns and use them to replace nouns in simple sentences.",
            teacherExplanationEnglish = "Explain that a pronoun is a word used instead of a noun so we do not repeat the same name many times. Start with a noun sentence, then rewrite it using a pronoun and ask pupils what changed.",
            supportText = "Use a short Efik support note to explain that pronouns can stand in place of naming words.",
            examples = listOf(
                "Mary is kind. She helps her friends.",
                "The boys are playing. They are happy.",
                "The book is new. It is on the desk."
            ),
            classActivity = "Write sentences with repeated nouns on the board and let pupils replace the repeated nouns with the correct pronouns.",
            quizQuestions = listOf(
                "What is a pronoun?",
                "Which pronoun can replace 'Mary' in a sentence?",
                "Which pronoun can replace 'the boys'?",
                "Which pronoun can replace 'the book'?",
                "Write one sentence that uses a pronoun."
            ),
            answerKey = listOf(
                "A pronoun is a word that replaces a noun.",
                "She.",
                "They.",
                "It.",
                "Accept any correct sentence with a pronoun."
            ),
            takeHomeRevision = "Write five nouns and the pronouns that can replace them."
        )
    }

    private fun verbLesson(request: LessonGenerationRequest, topic: String): Lesson {
        return buildLesson(
            request = request,
            topic = topic,
            lessonTitle = "Understanding Verbs",
            learningObjective = "Learners will be able to identify verbs as action or doing words in simple sentences.",
            teacherExplanationEnglish = "Tell the class that a verb shows action or state. Read short sentences aloud and let learners point to the action word in each one.",
            supportText = "Use a short Efik support note to explain that verbs show action in a sentence.",
            examples = listOf(
                "The girl runs to school.",
                "Mother cooks rice.",
                "The baby sleeps early."
            ),
            classActivity = "Act out common actions like run, jump, clap, and write the matching verbs on the board.",
            quizQuestions = listOf(
                "What is a verb?",
                "Which word is the verb in 'The boy sings well'?",
                "Name one verb that shows action.",
                "Which word is the verb in 'Mother cooks rice'?",
                "Write one sentence with a verb."
            ),
            answerKey = listOf(
                "A verb is an action or doing word.",
                "sings.",
                "Examples include run, jump, clap, or write.",
                "cooks.",
                "Accept any correct sentence with a verb."
            ),
            takeHomeRevision = "Write five action words you used today and put each one in a sentence."
        )
    }

    private fun adjectiveLesson(request: LessonGenerationRequest, topic: String): Lesson {
        return buildLesson(
            request = request,
            topic = topic,
            lessonTitle = "Understanding Adjectives",
            learningObjective = "Learners will be able to identify adjectives as describing words for nouns.",
            teacherExplanationEnglish = "Explain that adjectives tell us more about a noun. They describe size, color, number, or quality. Show a noun first, then add an adjective to make the meaning clearer.",
            supportText = "Use a short Efik support note to explain that adjectives describe people, places, animals, or things.",
            examples = listOf(
                "The tall boy carried a heavy bag.",
                "We saw a red car.",
                "She has three pencils."
            ),
            classActivity = "Show classroom objects and ask pupils to give one describing word for each object.",
            quizQuestions = listOf(
                "What is an adjective?",
                "Which word is the adjective in 'the red ball'?",
                "Name one adjective that describes size.",
                "Which word is the adjective in 'a happy child'?",
                "Write one sentence with an adjective."
            ),
            answerKey = listOf(
                "An adjective is a describing word.",
                "red.",
                "Examples include big, small, tall, or short.",
                "happy.",
                "Accept any correct sentence with an adjective."
            ),
            takeHomeRevision = "Describe five things at home using one adjective for each."
        )
    }

    private fun tenseLesson(request: LessonGenerationRequest, topic: String): Lesson {
        return buildLesson(
            request = request,
            topic = topic,
            lessonTitle = "Understanding Basic Tenses",
            learningObjective = "Learners will be able to distinguish simple present, past, and future tense in short sentences.",
            teacherExplanationEnglish = "Explain that tense tells us when an action happens. Use one action like 'play' and show how it changes in present, past, and future examples.",
            supportText = "Use a short Efik support note to explain that tense shows time in a sentence.",
            examples = listOf(
                "I play every day.",
                "I played yesterday.",
                "I will play tomorrow."
            ),
            classActivity = "Write mixed tense sentences on the board and ask pupils to group them into present, past, and future.",
            quizQuestions = listOf(
                "What does tense show?",
                "Which sentence is in the past tense: 'I play', 'I played', or 'I will play'?",
                "Which tense shows what is happening now?",
                "Which tense shows what will happen later?",
                "Write one sentence in the future tense."
            ),
            answerKey = listOf(
                "Tense shows the time of an action.",
                "'I played.'",
                "Present tense.",
                "Future tense.",
                "Accept any correct future-tense sentence."
            ),
            takeHomeRevision = "Write one sentence each in present, past, and future tense."
        )
    }

    private fun partsOfSpeechLesson(request: LessonGenerationRequest, topic: String): Lesson {
        return buildLesson(
            request = request,
            topic = topic,
            lessonTitle = "Parts of Speech Overview",
            learningObjective = "Learners will be able to identify nouns, pronouns, verbs, and adjectives in simple sentences.",
            teacherExplanationEnglish = "Give a quick overview that different words do different jobs in a sentence. Use one short sentence and point out the noun, pronoun, verb, and adjective.",
            supportText = "Use a short Efik support note to explain that words in a sentence have different jobs.",
            examples = listOf(
                "The happy boy runs fast.",
                "Mary is kind, and she helps others.",
                "The red ball rolled away."
            ),
            classActivity = "Write short sentences on the board and ask pupils to label each key word by its part of speech.",
            quizQuestions = listOf(
                "What is a noun?",
                "What is a pronoun?",
                "What is a verb?",
                "What is an adjective?",
                "Identify one noun and one verb in 'The girl sings loudly.'"
            ),
            answerKey = listOf(
                "A noun is a naming word.",
                "A pronoun replaces a noun.",
                "A verb is an action or doing word.",
                "An adjective is a describing word.",
                "girl is the noun; sings is the verb."
            ),
            takeHomeRevision = "Write four sentences and underline a different part of speech in each one."
        )
    }

    private fun sentenceLesson(request: LessonGenerationRequest, topic: String): Lesson {
        return buildLesson(
            request = request,
            topic = topic,
            lessonTitle = "Building Simple Sentences (SVO)",
            learningObjective = "Learners will be able to construct clear simple sentences in subject-verb-object order.",
            teacherExplanationEnglish = "A simple sentence can be built with three main parts. The subject tells us who or what acts. The verb shows the action. The object receives the action. Write one example on the board and let the class point out each part.",
            supportText = "Use a short Efik support note to explain subject, verb, and object with one easy example.",
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
            takeHomeRevision = "Write five simple sentences about your family using subject, verb, and object order."
        )
    }

    private fun buildLesson(
        request: LessonGenerationRequest,
        topic: String,
        lessonTitle: String,
        learningObjective: String,
        teacherExplanationEnglish: String,
        supportText: String,
        examples: List<String>,
        classActivity: String,
        quizQuestions: List<String>,
        answerKey: List<String>,
        takeHomeRevision: String
    ): Lesson {
        return Lesson(
            id = UUID.randomUUID().toString(),
            lessonTitle = lessonTitle,
            classLevel = request.classLevel,
            subject = request.subject,
            supportLanguage = request.supportLanguage,
            topic = topic,
            learningObjective = learningObjective,
            teacherExplanationEnglish = teacherExplanationEnglish,
            supportExplanation = SupportExplanation(
                language = request.supportLanguage,
                text = supportText
            ),
            examples = examples,
            classActivity = classActivity,
            quizQuestions = quizQuestions,
            answerKey = answerKey,
            takeHomeRevision = takeHomeRevision,
            createdAt = Instant.now().toString()
        )
    }
}
