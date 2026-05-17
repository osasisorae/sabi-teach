package com.sabiteach.nativeapp.generation

import com.sabiteach.nativeapp.model.Lesson

interface LessonGenerator {
    suspend fun availability(): GeneratorAvailability

    suspend fun generate(request: LessonGenerationRequest): Lesson
}
