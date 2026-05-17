package com.sabiteach.nativeapp.ui

import com.sabiteach.nativeapp.generation.GeneratorAvailability
import com.sabiteach.nativeapp.generation.GeneratorMode
import com.sabiteach.nativeapp.model.Lesson
import com.sabiteach.nativeapp.model.LessonMode

data class SabiTeachUiState(
    val topic: String = "Nouns",
    val currentLesson: Lesson? = null,
    val savedLessons: List<Lesson> = emptyList(),
    val selectedMode: LessonMode = LessonMode.Teacher,
    val isGenerating: Boolean = false,
    val generatorMode: GeneratorMode = GeneratorMode.Mock,
    val availability: GeneratorAvailability = GeneratorAvailability.Unknown,
    val errorMessage: String? = null
)
