import { generateMockLesson, type GenerateLessonInput } from "./mock-generator";
import type { Lesson } from "../types/lesson";

const GENERATION_MODE = process.env.EXPO_PUBLIC_SABITEACH_GENERATION_MODE ?? "mock";
const API_BASE_URL = process.env.EXPO_PUBLIC_SABITEACH_API_BASE_URL ?? "";

type RemoteLessonRequest = GenerateLessonInput & {
  classLevel: string;
  subject: string;
};

type RemoteLessonResponse = {
  lesson: Lesson;
};

async function generateRemoteLesson(input: RemoteLessonRequest): Promise<Lesson> {
  if (!API_BASE_URL) {
    throw new Error("Missing EXPO_PUBLIC_SABITEACH_API_BASE_URL for remote generation mode.");
  }

  const response = await fetch(`${API_BASE_URL}/generate-lesson`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(input)
  });

  if (!response.ok) {
    throw new Error(`Generation request failed with status ${response.status}.`);
  }

  const payload = (await response.json()) as Partial<RemoteLessonResponse>;

  if (!payload.lesson) {
    throw new Error("Remote generation response did not include a lesson.");
  }

  return payload.lesson;
}

export async function generateLesson(input: RemoteLessonRequest): Promise<Lesson> {
  if (GENERATION_MODE === "remote") {
    return generateRemoteLesson(input);
  }

  return generateMockLesson({
    topic: input.topic,
    supportLanguage: input.supportLanguage
  });
}

export function getGenerationMode(): string {
  return GENERATION_MODE;
}
