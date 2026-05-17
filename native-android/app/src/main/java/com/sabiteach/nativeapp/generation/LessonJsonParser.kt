package com.sabiteach.nativeapp.generation

import com.sabiteach.nativeapp.model.Lesson
import com.sabiteach.nativeapp.model.SupportExplanation
import org.json.JSONArray
import org.json.JSONObject
import java.time.Instant
import java.util.UUID

internal object LessonJsonParser {
    fun parseApiLessonResponse(responseText: String): Lesson {
        val root = JSONObject(responseText)
        val lesson = root.optJSONObject("lesson")
            ?: error("Lesson API response did not include a lesson object.")

        return parsePersistedLessonObject(lesson)
    }

    fun parseGeneratedLesson(responseText: String, request: LessonGenerationRequest): Lesson {
        val lesson = extractJsonObject(responseText)

        return Lesson(
            id = UUID.randomUUID().toString(),
            lessonTitle = lesson.optString("lesson_title").trim(),
            classLevel = request.classLevel,
            subject = request.subject,
            supportLanguage = request.supportLanguage,
            topic = request.topic,
            learningObjective = lesson.optString("learning_objective").trim(),
            teacherExplanationEnglish = lesson.optString("teacher_explanation_english").trim(),
            supportExplanation = parseSupportExplanation(
                lesson.optJSONObject("support_explanation"),
                request.supportLanguage
            ),
            examples = parseStringArray(lesson.optJSONArray("examples")),
            classActivity = lesson.optString("class_activity").trim(),
            quizQuestions = parseStringArray(lesson.optJSONArray("quiz_questions")),
            answerKey = parseStringArray(lesson.optJSONArray("answer_key")),
            takeHomeRevision = lesson.optString("take_home_revision").trim(),
            createdAt = Instant.now().toString()
        ).also(::validateLesson)
    }

    fun extractJsonObject(responseText: String): JSONObject {
        val trimmed = responseText.trim()
        val fencedMatch = CODE_FENCE_PATTERN.find(trimmed)
        val candidate = fencedMatch?.groupValues?.get(1)?.trim().orEmpty().ifEmpty {
            val firstBrace = trimmed.indexOf('{')
            val lastBrace = trimmed.lastIndexOf('}')
            require(firstBrace >= 0 && lastBrace > firstBrace) {
                "Model response did not contain a JSON object."
            }
            trimmed.substring(firstBrace, lastBrace + 1)
        }

        return JSONObject(candidate)
    }

    private fun parsePersistedLessonObject(lesson: JSONObject): Lesson {
        return Lesson(
            id = lesson.optString("id"),
            lessonTitle = lesson.optString("lesson_title").trim(),
            classLevel = lesson.optString("class_level"),
            subject = lesson.optString("subject"),
            supportLanguage = lesson.optString("support_language"),
            topic = lesson.optString("topic"),
            learningObjective = lesson.optString("learning_objective").trim(),
            teacherExplanationEnglish = lesson.optString("teacher_explanation_english").trim(),
            supportExplanation = parseSupportExplanation(lesson.optJSONObject("support_explanation")),
            examples = parseStringArray(lesson.optJSONArray("examples")),
            classActivity = lesson.optString("class_activity").trim(),
            quizQuestions = parseStringArray(lesson.optJSONArray("quiz_questions")),
            answerKey = parseStringArray(lesson.optJSONArray("answer_key")),
            takeHomeRevision = lesson.optString("take_home_revision").trim(),
            createdAt = lesson.optString("created_at")
        ).also(::validateLesson)
    }

    private fun parseSupportExplanation(
        candidate: JSONObject?,
        fallbackLanguage: String = ""
    ): SupportExplanation {
        return SupportExplanation(
            language = candidate?.optString("language")?.trim().orEmpty().ifEmpty { fallbackLanguage },
            text = candidate?.optString("text")?.trim().orEmpty()
        )
    }

    private fun parseStringArray(items: JSONArray?): List<String> {
        if (items == null) {
            return emptyList()
        }

        return buildList(items.length()) {
            for (index in 0 until items.length()) {
                val value = items.optString(index).trim()
                if (value.isNotEmpty()) {
                    add(value)
                }
            }
        }
    }

    private fun validateLesson(lesson: Lesson) {
        require(lesson.lessonTitle.isNotBlank()) { "Generated lesson title was empty." }
        require(lesson.learningObjective.isNotBlank()) { "Generated learning objective was empty." }
        require(lesson.teacherExplanationEnglish.isNotBlank()) { "Generated teacher explanation was empty." }
        require(lesson.examples.isNotEmpty()) { "Generated lesson included no examples." }
        require(lesson.quizQuestions.isNotEmpty()) { "Generated lesson included no quiz questions." }
        require(lesson.answerKey.isNotEmpty()) { "Generated lesson included no answer key." }
    }

    private val CODE_FENCE_PATTERN = Regex(
        pattern = "```(?:json)?\\s*(\\{.*?\\})\\s*```",
        options = setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE)
    )
}
