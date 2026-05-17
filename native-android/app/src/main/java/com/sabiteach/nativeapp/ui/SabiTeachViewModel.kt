package com.sabiteach.nativeapp.ui

import com.sabiteach.nativeapp.generation.LessonGenerationRequest
import com.sabiteach.nativeapp.generation.LessonGenerator
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
        SabiTeachUiState(savedLessons = lessonStore.loadLessons())
    )
    val uiState: StateFlow<SabiTeachUiState> = _uiState.asStateFlow()

    suspend fun refreshAvailability() {
        val availability = generator.availability()
        _uiState.update { it.copy(availability = availability) }
    }

    suspend fun generateLesson() {
        val topic = _uiState.value.topic.trim().ifEmpty { "Nouns" }
        _uiState.update { it.copy(isGenerating = true, errorMessage = null) }

        runCatching {
            generator.generate(LessonGenerationRequest(topic = topic))
        }.onSuccess { lesson ->
            _uiState.update {
                it.copy(
                    currentLesson = lesson,
                    isGenerating = false,
                    selectedMode = LessonMode.Teacher
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
}
