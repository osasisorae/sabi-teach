import {
  CLASS_LEVEL,
  PRIMARY_5_ENGLISH_TOPICS,
  SUBJECT
} from "../content/topics";
import {
  EFIK_CLASSROOM_PHRASES,
  EFIK_GLOSSARY_NOTES,
  EFIK_REVIEW_NOTE,
  SUPPORT_LANGUAGE
} from "../content/efik";
import type { Lesson, TopicSeed } from "../types/lesson";

type GenerateLessonInput = {
  topic: string;
  supportLanguage: string;
};

export type { GenerateLessonInput };

function findTopicSeed(topic: string): TopicSeed | undefined {
  const normalized = topic.trim().toLowerCase();
  return PRIMARY_5_ENGLISH_TOPICS.find((item) => {
    return item.title.toLowerCase() === normalized || item.id === normalized;
  });
}

function titleCase(value: string): string {
  return value
    .trim()
    .split(/\s+/)
    .map((part) => part.charAt(0).toUpperCase() + part.slice(1).toLowerCase())
    .join(" ");
}

function buildExamples(topicTitle: string): string[] {
  const lower = topicTitle.toLowerCase();

  if (lower.includes("noun")) {
    return ["Ada is a pupil.", "The market is busy today.", "The goat is under the tree."];
  }

  if (lower.includes("pronoun")) {
    return ["Emeka is late. He is running.", "The bag is new. It is red.", "The pupils are ready. They are smiling."];
  }

  if (lower.includes("verb")) {
    return ["The girl sings.", "Our teacher writes on the board.", "The children jump outside."];
  }

  if (lower.includes("adjective")) {
    return ["The tall boy waved.", "We saw a red ball.", "It was a happy day."];
  }

  if (lower.includes("tense")) {
    return ["I walk to school.", "I walked to school yesterday.", "I will walk to school tomorrow."];
  }

  if (lower.includes("sentence")) {
    return ["The boy kicked the ball.", "My mother cooked rice.", "The pupils read aloud."];
  }

  return ["The class reads together.", "The teacher explains the topic.", "The pupils answer the questions."];
}

function buildQuiz(topicTitle: string): { questions: string[]; answers: string[] } {
  const questions = [
    `What is ${topicTitle.toLowerCase()}?`,
    `Give one example of ${topicTitle.toLowerCase()}.`,
    `Write a short sentence that shows ${topicTitle.toLowerCase()}.`,
    `Why is ${topicTitle.toLowerCase()} important in English?`,
    `Circle or say the correct answer from one example your teacher gives.`
  ];

  const answers = [
    `${topicTitle} is explained in the lesson objective and examples.`,
    "Accept any correct example that matches the topic.",
    "Accept any simple correct sentence that matches the topic.",
    "It helps learners understand and use English correctly.",
    "Accept the correct classroom response."
  ];

  return { questions, answers };
}

function buildSupportExplanation(topicTitle: string, supportLanguage: string): string {
  const phrase = EFIK_CLASSROOM_PHRASES[0]?.support_text ?? "";
  const normalizedTopic = topicTitle.toLowerCase();
  const note = EFIK_GLOSSARY_NOTES.find((item) => item.toLowerCase().includes(normalizedTopic));

  if (supportLanguage !== SUPPORT_LANGUAGE) {
    return `Support language selected: ${supportLanguage}. This scaffold currently ships with ${SUPPORT_LANGUAGE} seed content only.`;
  }

  const glossaryHint = note ? `Glossary hint: ${note}` : "";

  return `Teacher support note in ${SUPPORT_LANGUAGE}: Explain ${topicTitle.toLowerCase()} with short familiar examples, then ask learners to repeat and answer in simple English. Sample prompt: ${phrase} ${glossaryHint} ${EFIK_REVIEW_NOTE}`;
}

export async function generateMockLesson({
  topic,
  supportLanguage
}: GenerateLessonInput): Promise<Lesson> {
  const cleanTopic = titleCase(topic || "Parts of Speech");
  const topicSeed = findTopicSeed(cleanTopic);
  const { questions, answers } = buildQuiz(cleanTopic);
  const examples = buildExamples(cleanTopic);

  const lesson: Lesson = {
    id: `lesson-${Date.now()}`,
    lesson_title: `${cleanTopic} Lesson`,
    class_level: CLASS_LEVEL,
    subject: SUBJECT,
    support_language: supportLanguage,
    topic: cleanTopic,
    learning_objective:
      topicSeed?.goal ??
      `Help learners understand the basic idea of ${cleanTopic.toLowerCase()} and use it in simple classroom examples.`,
    teacher_explanation_english: `${cleanTopic} should be explained with short, concrete examples that Primary 5 learners hear every day. Start with a simple definition, show two or three examples on the board, ask the class to repeat them, then let pupils give their own examples aloud.`,
    support_explanation: {
      language: supportLanguage,
      text: buildSupportExplanation(cleanTopic, supportLanguage)
    },
    examples,
    class_activity:
      "Write three examples on the board, let pupils identify the target word or pattern, then ask them to create one example each with a partner.",
    quiz_questions: questions,
    answer_key: answers,
    take_home_revision:
      `Ask learners to explain ${cleanTopic.toLowerCase()} to someone at home and write two new examples in their exercise book.`,
    created_at: new Date().toISOString()
  };

  await new Promise((resolve) => setTimeout(resolve, 600));

  return lesson;
}
