package com.sabiteach.nativeapp.generation

import com.sabiteach.nativeapp.model.Lesson
import com.sabiteach.nativeapp.model.SupportExplanation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ApiLessonGenerator(
    private val baseUrl: String
) : LessonGenerator {
    override suspend fun availability(): GeneratorAvailability {
        return if (baseUrl.isBlank()) GeneratorAvailability.Unavailable else GeneratorAvailability.Ready
    }

    override suspend fun generate(request: LessonGenerationRequest): Lesson = withContext(Dispatchers.IO) {
        val trimmedBaseUrl = baseUrl.trim().trimEnd('/')
        require(trimmedBaseUrl.isNotEmpty()) {
            "SABITEACH_API_BASE_URL is missing for native Android remote generation."
        }

        val connection = (URL("$trimmedBaseUrl/generate-lesson").openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            connectTimeout = 15_000
            readTimeout = 60_000
            doOutput = true
            setRequestProperty("Content-Type", "application/json")
            setRequestProperty("Accept", "application/json")
        }

        try {
            val payload = JSONObject(
                mapOf(
                    "classLevel" to request.classLevel,
                    "subject" to request.subject,
                    "supportLanguage" to request.supportLanguage,
                    "topic" to request.topic
                )
            )

            connection.outputStream.use { output ->
                output.write(payload.toString().toByteArray(Charsets.UTF_8))
            }

            val responseCode = connection.responseCode
            val responseText = readResponseBody(connection, responseCode in 200..299)

            if (responseCode !in 200..299) {
                error("Lesson API request failed: $responseCode ${responseText.ifBlank { "Unknown error" }}")
            }

            return@withContext parseLessonResponse(responseText)
        } finally {
            connection.disconnect()
        }
    }

    private fun parseLessonResponse(responseText: String): Lesson {
        val root = JSONObject(responseText)
        val lesson = root.optJSONObject("lesson")
            ?: error("Lesson API response did not include a lesson object.")

        return Lesson(
            id = lesson.optString("id"),
            lessonTitle = lesson.optString("lesson_title"),
            classLevel = lesson.optString("class_level"),
            subject = lesson.optString("subject"),
            supportLanguage = lesson.optString("support_language"),
            topic = lesson.optString("topic"),
            learningObjective = lesson.optString("learning_objective"),
            teacherExplanationEnglish = lesson.optString("teacher_explanation_english"),
            supportExplanation = parseSupportExplanation(lesson.optJSONObject("support_explanation")),
            examples = parseStringArray(lesson.optJSONArray("examples")),
            classActivity = lesson.optString("class_activity"),
            quizQuestions = parseStringArray(lesson.optJSONArray("quiz_questions")),
            answerKey = parseStringArray(lesson.optJSONArray("answer_key")),
            takeHomeRevision = lesson.optString("take_home_revision"),
            createdAt = lesson.optString("created_at")
        ).also { parsed ->
            require(parsed.lessonTitle.isNotBlank()) { "Lesson API returned an empty lesson title." }
            require(parsed.quizQuestions.isNotEmpty()) { "Lesson API returned no quiz questions." }
            require(parsed.answerKey.isNotEmpty()) { "Lesson API returned no answer key." }
        }
    }

    private fun parseSupportExplanation(candidate: JSONObject?): SupportExplanation {
        return SupportExplanation(
            language = candidate?.optString("language").orEmpty(),
            text = candidate?.optString("text").orEmpty()
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

    private fun readResponseBody(connection: HttpURLConnection, useInputStream: Boolean): String {
        val stream = if (useInputStream) connection.inputStream else connection.errorStream
        if (stream == null) {
            return ""
        }

        return BufferedReader(InputStreamReader(stream)).use { reader ->
            reader.readText()
        }
    }
}
