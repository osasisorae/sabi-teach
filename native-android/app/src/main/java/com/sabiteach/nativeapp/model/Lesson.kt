package com.sabiteach.nativeapp.model

data class SupportExplanation(
    val language: String,
    val text: String
)

data class Lesson(
    val id: String,
    val lessonTitle: String,
    val classLevel: String,
    val subject: String,
    val supportLanguage: String,
    val topic: String,
    val learningObjective: String,
    val teacherExplanationEnglish: String,
    val supportExplanation: SupportExplanation,
    val examples: List<String>,
    val classActivity: String,
    val quizQuestions: List<String>,
    val answerKey: List<String>,
    val takeHomeRevision: String,
    val createdAt: String
)

enum class LessonMode {
    Teacher,
    Handout,
    OralQuiz,
    BoardWork
}

data class StudentHandout(
    val title: String,
    val intro: String,
    val objective: String,
    val examples: List<String>,
    val practice: List<String>,
    val homework: String
)

data class OralQuizItem(
    val prompt: String,
    val expectedAnswer: String
)

data class BoardWork(
    val boardTitle: String,
    val boardObjective: String,
    val keyPoints: List<String>,
    val examples: List<String>,
    val quickTask: List<String>
)
