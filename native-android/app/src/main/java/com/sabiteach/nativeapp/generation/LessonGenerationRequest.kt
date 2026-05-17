package com.sabiteach.nativeapp.generation

data class LessonGenerationRequest(
    val classLevel: String = "Primary 5",
    val subject: String = "English",
    val topic: String,
    val supportLanguage: String = "Efik"
)

enum class GeneratorMode {
    Mock,
    RemoteApi,
    OnDevice
}

enum class GeneratorAvailability {
    Ready,
    Unsupported,
    Unavailable,
    Unknown
}
