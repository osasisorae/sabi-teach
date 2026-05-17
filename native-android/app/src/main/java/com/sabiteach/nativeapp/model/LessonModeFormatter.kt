package com.sabiteach.nativeapp.model

object LessonModeFormatter {
    fun toSentenceList(text: String, maxItems: Int): List<String> {
        return text
            .split(Regex("(?<=[.!?])\\s+"))
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .take(maxItems)
    }

    fun simplifySupportText(text: String, language: String): String {
        val base = text
            .replace(Regex("\\([^)]*\\)"), "")
            .replace(Regex("\\s+"), " ")
            .trim()

        val shorter = toSentenceList(base, 2).joinToString(" ").trim()
        return shorter.ifEmpty {
            "Use a short $language support note with one easy example, then return to English."
        }
    }

    fun buildTeacherSteps(lesson: Lesson): List<String> {
        return listOf(
            "Start with the lesson goal: ${lesson.learningObjective}",
            "Explain the topic in simple English: ${toSentenceList(lesson.teacherExplanationEnglish, 2).joinToString(" ")}",
            "Bridge with a short ${lesson.supportLanguage} support note: ${simplifySupportText(lesson.supportExplanation.text, lesson.supportLanguage)}",
            "Read aloud the examples: ${lesson.examples.take(2).joinToString(" | ")}",
            "Run the classroom activity: ${lesson.classActivity}"
        )
    }

    fun buildStudentHandout(lesson: Lesson): StudentHandout {
        return StudentHandout(
            title = "${lesson.lessonTitle} Handout",
            intro = "Today we are learning ${lesson.topic.lowercase()}.",
            objective = lesson.learningObjective,
            examples = lesson.examples,
            practice = lesson.quizQuestions,
            homework = lesson.takeHomeRevision
        )
    }

    fun buildOralQuiz(lesson: Lesson): List<OralQuizItem> {
        return lesson.quizQuestions.mapIndexed { index, question ->
            OralQuizItem(
                prompt = question,
                expectedAnswer = lesson.answerKey.getOrNull(index) ?: "Accept a correct response."
            )
        }
    }

    fun buildBoardWork(lesson: Lesson): BoardWork {
        return BoardWork(
            boardTitle = lesson.lessonTitle,
            boardObjective = lesson.learningObjective,
            keyPoints = toSentenceList(lesson.teacherExplanationEnglish, 3),
            examples = lesson.examples.take(3),
            quickTask = lesson.quizQuestions.take(2)
        )
    }
}
