import type { Lesson } from "../types/lesson";

export type LessonMode = "teacher" | "handout" | "oral" | "board";

export function toSentenceList(text: string, maxItems: number): string[] {
  return text
    .split(/(?<=[.!?])\s+/)
    .map((item) => item.trim())
    .filter(Boolean)
    .slice(0, maxItems);
}

export function simplifySupportText(text: string, language: string): string {
  const base = text
    .replace(/\([^)]*\)/g, "")
    .replace(/\s+/g, " ")
    .trim();

  const shorter = toSentenceList(base, 2).join(" ").trim();
  return shorter || `Use a short ${language} support note with one easy example, then return to English.`;
}

export function buildTeacherSteps(lesson: Lesson): string[] {
  return [
    `Start by telling the class the lesson goal: ${lesson.learning_objective}`,
    `Explain the topic in simple English: ${toSentenceList(lesson.teacher_explanation_english, 2).join(" ")}`,
    `Bridge with a short ${lesson.support_language} support note: ${simplifySupportText(
      lesson.support_explanation.text,
      lesson.support_language
    )}`,
    `Use the examples aloud with the class: ${lesson.examples.slice(0, 2).join(" | ")}`,
    `Run the classroom activity: ${lesson.class_activity}`
  ];
}

export function buildStudentHandout(lesson: Lesson) {
  return {
    title: `${lesson.lesson_title} Handout`,
    intro: `Today we are learning ${lesson.topic.toLowerCase()}.`,
    objective: lesson.learning_objective,
    examples: lesson.examples,
    practice: lesson.quiz_questions,
    homework: lesson.take_home_revision
  };
}

export function buildOralQuiz(lesson: Lesson) {
  return lesson.quiz_questions.map((question, index) => ({
    prompt: question,
    expectedAnswer: lesson.answer_key[index] ?? "Accept a correct response."
  }));
}

export function buildBoardWork(lesson: Lesson) {
  return {
    boardTitle: lesson.lesson_title,
    boardObjective: lesson.learning_objective,
    keyPoints: toSentenceList(lesson.teacher_explanation_english, 3),
    examples: lesson.examples.slice(0, 3),
    quickTask: lesson.quiz_questions.slice(0, 2)
  };
}
