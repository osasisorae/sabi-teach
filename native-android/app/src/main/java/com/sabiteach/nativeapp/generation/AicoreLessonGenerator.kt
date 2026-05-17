package com.sabiteach.nativeapp.generation

import android.content.Context
import com.sabiteach.nativeapp.model.Lesson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AicoreLessonGenerator(
    context: Context
) : LessonGenerator {
    override val mode: GeneratorMode = GeneratorMode.OnDevice

    @Suppress("unused")
    private val appContext = context.applicationContext

    private val promptClient = AicorePromptClient()

    override suspend fun availability(): GeneratorAvailability = withContext(Dispatchers.Default) {
        val status = runCatching { promptClient.checkStatus() }
            .getOrElse { return@withContext GeneratorAvailability.Unavailable }

        return@withContext when (status) {
            AicorePromptClient.STATUS_AVAILABLE -> GeneratorAvailability.Ready
            AicorePromptClient.STATUS_DOWNLOADABLE -> GeneratorAvailability.DownloadRequired
            AicorePromptClient.STATUS_DOWNLOADING -> GeneratorAvailability.Downloading
            AicorePromptClient.STATUS_UNAVAILABLE -> GeneratorAvailability.Unavailable
            else -> GeneratorAvailability.Unknown
        }
    }

    override suspend fun generate(request: LessonGenerationRequest): Lesson = withContext(Dispatchers.Default) {
        val prompt = PromptTemplates.lessonPrompt(request)
        val responseText = runCatching {
            promptClient.ensureReady()
            promptClient.generateText(prompt, 256)
        }.getOrElse { error ->
            throw IllegalStateException(userFacingMessage(error), error)
        }

        return@withContext LessonJsonParser.parseGeneratedLesson(responseText, request)
    }

    private fun userFacingMessage(error: Throwable): String {
        val message = error.message.orEmpty()
        return when {
            "Status=${AicorePromptClient.STATUS_DOWNLOADING}" in message ->
                "Gemini Nano is still downloading on this device. Keep the device online and try again."
            "Status=${AicorePromptClient.STATUS_DOWNLOADABLE}" in message ->
                "Gemini Nano still needs to be downloaded on this device."
            "Status=${AicorePromptClient.STATUS_UNAVAILABLE}" in message ->
                "Gemini Nano is unavailable on this device. AICore support is required for on-device generation."
            message.isNotBlank() -> message
            else -> "On-device generation failed."
        }
    }
}
