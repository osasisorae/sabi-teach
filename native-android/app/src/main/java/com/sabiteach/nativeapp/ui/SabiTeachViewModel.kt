package com.sabiteach.nativeapp.ui

import com.sabiteach.nativeapp.generation.LessonGenerationRequest
import com.sabiteach.nativeapp.generation.LessonGenerator
import com.sabiteach.nativeapp.generation.GeneratorAvailability
import com.sabiteach.nativeapp.generation.GeneratorMode
import com.sabiteach.nativeapp.model.Lesson
import com.sabiteach.nativeapp.model.LessonMode
import com.sabiteach.nativeapp.storage.LessonStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SabiTeachViewModel(
    private val generator: LessonGenerator,
    private val lessonStore: LessonStore
) {
    private val _uiState = MutableStateFlow(
        SabiTeachUiState(
            savedLessons = lessonStore.loadLessons(),
            generatorMode = generator.mode
        )
    )
    val uiState: StateFlow<SabiTeachUiState> = _uiState.asStateFlow()

    suspend fun refreshAvailability() {
        val availability = generator.availability()
        _uiState.update { it.copy(generatorMode = generator.mode, availability = availability) }
    }

    suspend fun generateLesson() {
        val topic = _uiState.value.topic.trim().ifEmpty { "Nouns" }
        val availability = generator.availability()
        _uiState.update {
            it.copy(
                isGenerating = false,
                errorMessage = null,
                generatorMode = generator.mode,
                availability = availability
            )
        }

        val canGenerate = availability == GeneratorAvailability.Ready ||
            availability == GeneratorAvailability.DownloadRequired

        if (!canGenerate) {
            _uiState.update {
                it.copy(
                    errorMessage = availabilityMessage(generator.mode, availability)
                )
            }
            return
        }

        _uiState.update { it.copy(isGenerating = true, errorMessage = null) }

        runCatching {
            generator.generate(LessonGenerationRequest(topic = topic))
        }.onSuccess { lesson ->
            _uiState.update {
                it.copy(
                    currentLesson = lesson,
                    isGenerating = false,
                    selectedMode = LessonMode.Teacher,
                    availability = availability
                )
            }
        }.onFailure { error ->
            _uiState.update {
                it.copy(
                    isGenerating = false,
                    errorMessage = error.message ?: "Native lesson generation failed."
                )
            }
        }
    }

    fun updateTopic(topic: String) {
        _uiState.update { it.copy(topic = topic) }
    }

    fun selectMode(mode: LessonMode) {
        _uiState.update { it.copy(selectedMode = mode) }
    }

    fun saveCurrentLesson() {
        val lesson = _uiState.value.currentLesson ?: return
        val nextLessons = listOf(lesson) + _uiState.value.savedLessons.filterNot { saved ->
            saved.id == lesson.id
        }

        runCatching {
            lessonStore.saveLessons(nextLessons)
        }.onSuccess {
            _uiState.update { it.copy(savedLessons = nextLessons, errorMessage = null) }
        }.onFailure { error ->
            _uiState.update {
                it.copy(
                    errorMessage = error.message ?: "Failed to save lesson locally."
                )
            }
        }
    }

    fun openSavedLesson(lesson: Lesson) {
        _uiState.update { it.copy(currentLesson = lesson) }
    }

    private fun availabilityMessage(
        mode: GeneratorMode,
        availability: GeneratorAvailability
    ): String {
        return when (mode) {
            GeneratorMode.Mock -> "Mock generator should always be available in this build."
            GeneratorMode.RemoteApi -> when (availability) {
                GeneratorAvailability.DownloadRequired ->
                    "Remote API mode does not use downloadable model assets."
                GeneratorAvailability.Downloading ->
                    "Remote API mode does not download model assets on device."
                GeneratorAvailability.Unavailable ->
                    "Remote API mode is selected, but SABITEACH_API_BASE_URL is missing."
                GeneratorAvailability.Unsupported ->
                    "Remote API mode is not supported in this build."
                GeneratorAvailability.Unknown ->
                    "Remote API status is still being checked."
                GeneratorAvailability.Ready -> ""
            }

            GeneratorMode.OnDevice -> when (availability) {
                GeneratorAvailability.DownloadRequired ->
                    "On-device generation needs Gemini Nano to download first. Keep the device online, then generate again."
                GeneratorAvailability.Downloading ->
                    "Gemini Nano is downloading on this device. Wait for the download to finish, then try again."
                GeneratorAvailability.Unsupported ->
                    "On-device generation is not supported in this build."
                GeneratorAvailability.Unavailable ->
                    "On-device generation is unavailable on this device."
                GeneratorAvailability.Unknown ->
                    "On-device generation support is still being checked."
                GeneratorAvailability.Ready -> ""
            }
        }
    }
}
