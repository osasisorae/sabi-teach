package com.sabiteach.nativeapp.generation

import com.sabiteach.nativeapp.model.Lesson

class AicoreLessonGenerator : LessonGenerator {
    override suspend fun availability(): GeneratorAvailability {
        return GeneratorAvailability.Unknown
    }

    override suspend fun generate(request: LessonGenerationRequest): Lesson {
        error(
            """
            Real on-device generation is not wired yet.

            Next integration target:
            - ML Kit Prompt API
            - AICore-supported Android device
            - Gemma 4 preview model selection

            Use PromptTemplates.lessonPrompt(request) as the structured prompt source.
            Parse the model JSON into the Lesson model and keep the same teaching modes used in Expo.
            """.trimIndent()
        )
    }
}
