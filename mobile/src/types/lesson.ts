export type SupportExplanation = {
  language: string;
  text: string;
};

export type Lesson = {
  id: string;
  lesson_title: string;
  class_level: string;
  subject: "English";
  support_language: string;
  topic: string;
  learning_objective: string;
  teacher_explanation_english: string;
  support_explanation: SupportExplanation;
  examples: string[];
  class_activity: string;
  quiz_questions: string[];
  answer_key: string[];
  take_home_revision: string;
  created_at: string;
};

export type TopicSeed = {
  id: string;
  title: string;
  goal: string;
};
