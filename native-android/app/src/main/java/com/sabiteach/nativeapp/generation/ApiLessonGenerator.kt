package com.sabiteach.nativeapp.generation

import com.sabiteach.nativeapp.model.Lesson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ApiLessonGenerator(
    private val baseUrl: String
) : LessonGenerator {
    override val mode: GeneratorMode = GeneratorMode.RemoteApi

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

            return@withContext LessonJsonParser.parseApiLessonResponse(responseText)
        } finally {
            connection.disconnect()
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
