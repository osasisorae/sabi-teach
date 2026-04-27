import AsyncStorage from "@react-native-async-storage/async-storage";

import type { Lesson } from "../types/lesson";

const SAVED_LESSONS_KEY = "sabiteach.saved_lessons";

export async function loadSavedLessons(): Promise<Lesson[]> {
  const raw = await AsyncStorage.getItem(SAVED_LESSONS_KEY);
  if (!raw) {
    return [];
  }

  try {
    const parsed = JSON.parse(raw) as Lesson[];
    return Array.isArray(parsed) ? parsed : [];
  } catch {
    return [];
  }
}

export async function saveLesson(lesson: Lesson): Promise<Lesson[]> {
  const existing = await loadSavedLessons();
  const next = [lesson, ...existing.filter((item) => item.id !== lesson.id)];
  await AsyncStorage.setItem(SAVED_LESSONS_KEY, JSON.stringify(next));
  return next;
}
