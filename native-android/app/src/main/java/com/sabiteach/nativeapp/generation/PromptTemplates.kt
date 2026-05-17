package com.sabiteach.nativeapp.generation

object PromptTemplates {
    fun lessonPrompt(request: LessonGenerationRequest): String {
        return """
            You are generating one classroom-ready lesson for a teacher in Nigeria.

            Return valid JSON only.

            Requirements:
            - Class level: ${request.classLevel}
            - Subject: ${request.subject}
            - Topic: ${request.topic}
            - Support language: ${request.supportLanguage}
            - Keep the English explanation simple and teacher-friendly.
            - Keep the ${request.supportLanguage} support explanation short and careful.
            - Return five quiz questions.
            - Return five matching answer-key items.
            - Return three simple examples.
            - Make the class activity practical in a real classroom with limited resources.

            JSON shape:
            {
              "lesson_title": "string",
              "learning_objective": "string",
              "teacher_explanation_english": "string",
              "support_explanation": {
                "language": "${request.supportLanguage}",
                "text": "string"
              },
              "examples": ["string", "string", "string"],
              "class_activity": "string",
              "quiz_questions": ["string", "string", "string", "string", "string"],
              "answer_key": ["string", "string", "string", "string", "string"],
              "take_home_revision": "string"
            }
        """.trimIndent()
    }
}
