package com.sabiteach.nativeapp.storage

import android.content.Context
import com.sabiteach.nativeapp.model.Lesson
import com.sabiteach.nativeapp.model.SupportExplanation
import org.json.JSONArray
import org.json.JSONObject

class SharedPreferencesLessonStore(
    context: Context
) : LessonStore {
    private val sharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun loadLessons(): List<Lesson> {
        val raw = sharedPreferences.getString(SAVED_LESSONS_KEY, null) ?: return emptyList()

        return runCatching {
            val root = JSONArray(raw)
            buildList(root.length()) {
                for (index in 0 until root.length()) {
                    val lesson = root.optJSONObject(index) ?: continue
                    add(lesson.toLesson())
                }
            }
        }.getOrDefault(emptyList())
    }

    override fun saveLessons(lessons: List<Lesson>) {
        val payload = JSONArray().apply {
            lessons.forEach { lesson ->
                put(lesson.toJson())
            }
        }

        val committed = sharedPreferences.edit()
            .putString(SAVED_LESSONS_KEY, payload.toString())
            .commit()

        check(committed) {
            "Failed to save lessons locally on this device."
        }
    }

    private fun JSONObject.toLesson(): Lesson {
        return Lesson(
            id = optString("id"),
            lessonTitle = optString("lessonTitle"),
            classLevel = optString("classLevel"),
            subject = optString("subject"),
            supportLanguage = optString("supportLanguage"),
            topic = optString("topic"),
            learningObjective = optString("learningObjective"),
            teacherExplanationEnglish = optString("teacherExplanationEnglish"),
            supportExplanation = SupportExplanation(
                language = optJSONObject("supportExplanation")?.optString("language").orEmpty(),
                text = optJSONObject("supportExplanation")?.optString("text").orEmpty()
            ),
            examples = optJSONArray("examples").toStringList(),
            classActivity = optString("classActivity"),
            quizQuestions = optJSONArray("quizQuestions").toStringList(),
            answerKey = optJSONArray("answerKey").toStringList(),
            takeHomeRevision = optString("takeHomeRevision"),
            createdAt = optString("createdAt")
        )
    }

    private fun Lesson.toJson(): JSONObject {
        return JSONObject(
            mapOf(
                "id" to id,
                "lessonTitle" to lessonTitle,
                "classLevel" to classLevel,
                "subject" to subject,
                "supportLanguage" to supportLanguage,
                "topic" to topic,
                "learningObjective" to learningObjective,
                "teacherExplanationEnglish" to teacherExplanationEnglish,
                "supportExplanation" to JSONObject(
                    mapOf(
                        "language" to supportExplanation.language,
                        "text" to supportExplanation.text
                    )
                ),
                "examples" to JSONArray(examples),
                "classActivity" to classActivity,
                "quizQuestions" to JSONArray(quizQuestions),
                "answerKey" to JSONArray(answerKey),
                "takeHomeRevision" to takeHomeRevision,
                "createdAt" to createdAt
            )
        )
    }

    private fun JSONArray?.toStringList(): List<String> {
        if (this == null) {
            return emptyList()
        }

        return buildList(length()) {
            for (index in 0 until length()) {
                val value = optString(index).trim()
                if (value.isNotEmpty()) {
                    add(value)
                }
            }
        }
    }

    private companion object {
        const val PREFS_NAME = "sabiteach_native_storage"
        const val SAVED_LESSONS_KEY = "saved_lessons"
    }
}
