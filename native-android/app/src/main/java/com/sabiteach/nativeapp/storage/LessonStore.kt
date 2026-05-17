package com.sabiteach.nativeapp.storage

import com.sabiteach.nativeapp.model.Lesson

interface LessonStore {
    fun loadLessons(): List<Lesson>

    fun saveLessons(lessons: List<Lesson>)
}
