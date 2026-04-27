import { createServer } from "node:http";
import { readFile } from "node:fs/promises";
import path from "node:path";
import { fileURLToPath } from "node:url";

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const projectRoot = path.resolve(__dirname, "..");

const env = {
  PORT: Number(process.env.PORT ?? 4000),
  SABITEACH_PROVIDER: process.env.SABITEACH_PROVIDER ?? "mock",
  OLLAMA_BASE_URL: process.env.OLLAMA_BASE_URL ?? "http://127.0.0.1:11434",
  OLLAMA_MODEL: process.env.OLLAMA_MODEL ?? "gemma4",
  OPENAI_BASE_URL: process.env.OPENAI_BASE_URL ?? "https://api.openai.com/v1",
  OPENAI_API_KEY: process.env.OPENAI_API_KEY ?? "",
  OPENAI_MODEL: process.env.OPENAI_MODEL ?? ""
};

const topicPack = await readJson(path.join(projectRoot, "content", "primary5-english", "topics.json"));
const efikGlossary = await readJson(
  path.join(projectRoot, "content", "language-packs", "efik", "glossary.json")
);
const efikPhrases = await readJson(
  path.join(projectRoot, "content", "language-packs", "efik", "classroom-phrases.json")
);

const TOPIC_GUIDES = {
  nouns: {
    lessonTitle: "Understanding Nouns",
    teacherExplanation:
      "Tell the class that a noun is a naming word. A noun can name a person, place, animal, or thing. Write simple examples on the board like teacher, market, goat, and book. Read each example aloud and ask learners to say whether it names a person, place, animal, or thing.",
    supportExplanation:
      "Use a short Efik support note to explain that nouns are naming words for people, places, animals, and things, then return to simple English examples.",
    examples: [
      "The teacher entered the classroom.",
      "The goat ran across the road.",
      "My book is on the desk."
    ],
    classActivity:
      "Write ten words on the board and ask pupils to sort them into people, places, animals, and things.",
    quizQuestions: [
      "What is a noun?",
      "Which word is the noun in the sentence 'The boy kicked the ball'?",
      "Name one noun that is a place.",
      "Name one noun that is an animal.",
      "Write one sentence with a noun."
    ],
    answerKey: [
      "A noun is a naming word for a person, place, animal, or thing.",
      "boy and ball are nouns.",
      "Examples include school, market, or village.",
      "Examples include goat, dog, or bird.",
      "Accept any correct sentence with a noun."
    ],
    takeHomeRevision:
      "Write four nouns at home: one person, one place, one animal, and one thing."
  },
  pronouns: {
    lessonTitle: "Understanding Pronouns",
    teacherExplanation:
      "Explain that a pronoun is a word we use instead of a noun. It helps us avoid repeating the same name many times. Write examples like 'Ada is here. She is ready.' and 'The boys are late. They are running.' Ask learners to replace nouns with the correct pronouns.",
    supportExplanation:
      "Use a short Efik support note to explain that pronouns replace naming words like he, she, it, and they.",
    examples: [
      "Ada is my friend. She is kind.",
      "The ball is new. It is red.",
      "The boys are ready. They are smiling."
    ],
    classActivity:
      "Write five short sentence pairs on the board and let pupils replace repeated nouns with pronouns.",
    quizQuestions: [
      "What is a pronoun?",
      "Which pronoun can replace 'Mary'?",
      "Which pronoun can replace 'the boys'?",
      "Which pronoun can replace 'the book'?",
      "Write one sentence that uses a pronoun."
    ],
    answerKey: [
      "A pronoun is a word used in place of a noun.",
      "She.",
      "They.",
      "It.",
      "Accept any correct sentence that uses a pronoun."
    ],
    takeHomeRevision:
      "Write three sentences at home and replace one noun in each sentence with a pronoun."
  },
  verbs: {
    lessonTitle: "Understanding Verbs",
    teacherExplanation:
      "Tell learners that a verb is an action word. It shows what someone or something is doing. Write examples like run, sing, write, and jump. Then use them in short sentences such as 'The girl sings' and 'My father drives.' Ask learners to identify the action word in each sentence.",
    supportExplanation:
      "Use a short Efik support note to explain that verbs are action words that show what a person or thing does.",
    examples: [
      "The girl sings.",
      "My father drives.",
      "We read every day."
    ],
    classActivity:
      "Act out simple actions and ask learners to say the correct verb for each action.",
    quizQuestions: [
      "What is a verb?",
      "Which word is the verb in 'The dog barked loudly'?",
      "Which word is the verb in 'We play football'?",
      "Name one verb.",
      "Write one sentence with a verb."
    ],
    answerKey: [
      "A verb is an action word.",
      "barked.",
      "play.",
      "Examples include run, jump, read, or sing.",
      "Accept any correct sentence with a verb."
    ],
    takeHomeRevision:
      "Write five action words you used today and put each one in a short sentence."
  },
  adjectives: {
    lessonTitle: "Understanding Adjectives",
    teacherExplanation:
      "Explain that an adjective is a describing word. It tells us more about a noun by showing size, colour, number, or feeling. Write examples like red, tall, happy, and small. Then place them in short sentences so learners can see how the adjective describes the noun.",
    supportExplanation:
      "Use a short Efik support note to explain that adjectives describe nouns and tell us more about them.",
    examples: [
      "The red ball rolled away.",
      "The tall boy waved.",
      "She has a happy face."
    ],
    classActivity:
      "Show classroom objects and ask learners to describe each one with one adjective.",
    quizQuestions: [
      "What is an adjective?",
      "Which word describes the noun in 'The red ball rolled'?",
      "Name one adjective for size.",
      "Name one adjective for colour.",
      "Write one sentence with an adjective."
    ],
    answerKey: [
      "An adjective is a describing word.",
      "red.",
      "Examples include big, small, tall, or short.",
      "Examples include red, blue, green, or yellow.",
      "Accept any correct sentence with an adjective."
    ],
    takeHomeRevision:
      "Describe three things at home using one adjective for each thing."
  },
  "simple sentence structure": {
    lessonTitle: "Building Simple Sentences (SVO)",
    teacherExplanation:
      "A simple sentence can be built with three main parts: subject, verb, and object. The subject is who or what acts, the verb is the action, and the object receives the action. Write examples like 'The boy kicked the ball' and 'Mama cooked rice' so learners can see the pattern clearly.",
    supportExplanation:
      "Use a short Efik support note to explain the order subject, verb, object with one simple classroom example.",
    examples: [
      "The girl reads a book.",
      "Mama cooks rice.",
      "The dog chases the ball."
    ],
    classActivity:
      "Give pupils subject, verb, and object word cards and ask them to arrange them into correct sentences.",
    quizQuestions: [
      "What are the three main parts of a simple SVO sentence?",
      "In 'The boy kicked the ball', which word is the subject?",
      "In 'Mama cooked rice', which word is the verb?",
      "In 'The dog chased the ball', which words are the object?",
      "Write one simple SVO sentence."
    ],
    answerKey: [
      "Subject, verb, and object.",
      "The boy.",
      "cooked.",
      "the ball.",
      "Accept any correct simple sentence in subject-verb-object order."
    ],
    takeHomeRevision:
      "Write five simple sentences about your family using subject, verb, and object order."
  },
  "parts of speech overview": {
    lessonTitle: "Understanding Word Roles: Parts of Speech",
    teacherExplanation:
      "Tell learners that words have different jobs in a sentence. Nouns name people, places, animals, or things. Verbs show actions. Adjectives describe nouns. Pronouns replace nouns. Use one short sentence at a time so learners can identify the job of each word clearly.",
    supportExplanation:
      "Use a short Efik support note to explain noun, verb, adjective, and pronoun as different word roles. Keep it simple and careful.",
    examples: [
      "The boy ran quickly.",
      "She ate a big apple.",
      "The red ball rolled."
    ],
    classActivity:
      "Call out words and ask pupils to place each one under Noun, Verb, Adjective, or Pronoun on the board.",
    quizQuestions: [
      "What does a noun do in a sentence?",
      "What does a verb do in a sentence?",
      "What does an adjective do in a sentence?",
      "What does a pronoun do in a sentence?",
      "Give one example each of a noun and a verb."
    ],
    answerKey: [
      "A noun names a person, place, animal, or thing.",
      "A verb shows an action or doing word.",
      "An adjective describes a noun.",
      "A pronoun replaces a noun.",
      "Examples include boy for noun and run for verb."
    ],
    takeHomeRevision:
      "Find one noun, one verb, one adjective, and one pronoun in your textbook and write them down."
  },
  "basic tenses": {
    lessonTitle: "Understanding Basic Tenses",
    teacherExplanation:
      "Explain that tense shows when an action happens. Present tense tells what happens now, past tense tells what happened before now, and future tense tells what will happen later. Use easy examples like walk, walked, and will walk so learners can hear the time change clearly.",
    supportExplanation:
      "Use a short Efik support note to explain now, before now, and later when teaching present, past, and future tense.",
    examples: [
      "I walk to school.",
      "I walked to school yesterday.",
      "I will walk to school tomorrow."
    ],
    classActivity:
      "Write one action on the board and let pupils say it in present, past, and future tense.",
    quizQuestions: [
      "What does tense show?",
      "Which tense shows an action happening now?",
      "Which tense shows an action that already happened?",
      "Which tense shows an action that will happen later?",
      "Change 'I play' to past tense."
    ],
    answerKey: [
      "Tense shows when an action happens.",
      "Present tense.",
      "Past tense.",
      "Future tense.",
      "I played."
    ],
    takeHomeRevision:
      "Write one sentence each in present tense, past tense, and future tense."
  }
};

const server = createServer(async (req, res) => {
  try {
    setCorsHeaders(res);

    if (req.method === "OPTIONS") {
      res.writeHead(204);
      res.end();
      return;
    }

    if (req.method === "GET" && req.url === "/health") {
      sendJson(res, 200, {
        ok: true,
        provider: env.SABITEACH_PROVIDER,
        port: env.PORT
      });
      return;
    }

    if (req.method === "POST" && req.url === "/generate-lesson") {
      const requestBody = await readJsonBody(req);
      const parsedInput = validateRequestBody(requestBody);
      const lesson = await generateLesson(parsedInput);

      sendJson(res, 200, {
        lesson
      });
      return;
    }

    sendJson(res, 404, {
      error: "Not found."
    });
  } catch (error) {
    const message = error instanceof Error ? error.message : "Unknown server error.";
    sendJson(res, 500, {
      error: message
    });
  }
});

server.listen(env.PORT, () => {
  console.log(`[sabiteach-server] listening on http://localhost:${env.PORT}`);
  console.log(`[sabiteach-server] provider=${env.SABITEACH_PROVIDER}`);
});

async function generateLesson(input) {
  const promptContext = buildPromptContext(input);

  if (env.SABITEACH_PROVIDER === "mock") {
    return buildFallbackLesson(input, "Using local mock provider.");
  }

  if (env.SABITEACH_PROVIDER === "ollama") {
    return generateWithOllama(input, promptContext);
  }

  if (env.SABITEACH_PROVIDER === "openai") {
    return generateWithOpenAICompatible(input, promptContext);
  }

  throw new Error(`Unsupported SABITEACH_PROVIDER: ${env.SABITEACH_PROVIDER}`);
}

async function generateWithOllama(input, promptContext) {
  const response = await fetch(`${env.OLLAMA_BASE_URL}/api/generate`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify({
      model: env.OLLAMA_MODEL,
      stream: false,
      prompt: promptContext.prompt,
      system: promptContext.system,
      format: "json",
      options: {
        temperature: 0.3
      }
    })
  });

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(`Ollama request failed: ${response.status} ${errorText}`);
  }

  const payload = await response.json();
  const rawText = typeof payload.response === "string" ? payload.response : "";
  const lesson = parseAndNormalizeLesson(rawText, input, "ollama");

  return lesson;
}

async function generateWithOpenAICompatible(input, promptContext) {
  if (!env.OPENAI_API_KEY) {
    throw new Error("OPENAI_API_KEY is required when SABITEACH_PROVIDER=openai.");
  }

  if (!env.OPENAI_MODEL) {
    throw new Error("OPENAI_MODEL is required when SABITEACH_PROVIDER=openai.");
  }

  const response = await fetch(`${env.OPENAI_BASE_URL}/chat/completions`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${env.OPENAI_API_KEY}`
    },
    body: JSON.stringify({
      model: env.OPENAI_MODEL,
      temperature: 0.3,
      messages: [
        {
          role: "system",
          content: promptContext.system
        },
        {
          role: "user",
          content: promptContext.prompt
        }
      ]
    })
  });

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(`OpenAI-compatible request failed: ${response.status} ${errorText}`);
  }

  const payload = await response.json();
  const rawText = payload?.choices?.[0]?.message?.content;

  if (typeof rawText !== "string") {
    throw new Error("OpenAI-compatible response did not include text content.");
  }

  return parseAndNormalizeLesson(rawText, input, "openai-compatible");
}

function buildPromptContext(input) {
  const topicEntry = topicPack.topics.find((item) => item.title.toLowerCase() === input.topic.toLowerCase());
  const topicGuide = getTopicGuide(input.topic);
  const glossaryHints = efikGlossary.entries
    .map((entry) => `- ${entry.term}: ${entry.english_definition} Hint: ${entry.support_note}`)
    .join("\n");
  const phraseHints = efikPhrases.phrases
    .map((entry) => `- ${entry.english} => ${entry.support_text}`)
    .join("\n");
  const topicGoal = topicEntry?.goal ?? `Teach ${input.topic} clearly to Primary 5 learners.`;

  const system = [
    "You are SabiTeach, a lesson-generation assistant for low-connectivity Nigerian classrooms.",
    "Return one valid JSON object only.",
    "Do not wrap JSON in markdown fences.",
    "Keep the English explanation simple, practical, and teacher-friendly.",
    "Keep the Efik support note short, cautious, and clearly instructional.",
    "Do not invent curriculum standards or long academic jargon.",
    "Do not mix multiple grammar concepts unless the topic is an overview topic.",
    "Do not produce vague answer keys like 'accept any' unless the question explicitly asks for a learner's own example.",
    "Use short classroom language suitable for Primary 5."
  ].join(" ");

  const prompt = [
    "Generate a Primary 5 English lesson as strict JSON with these keys only:",
    "lesson_title, class_level, subject, support_language, topic, learning_objective, teacher_explanation_english, support_explanation, examples, class_activity, quiz_questions, answer_key, take_home_revision.",
    'The support_explanation field must be an object with keys: language, text.',
    `Class level: ${input.classLevel}.`,
    `Subject: ${input.subject}.`,
    `Support language: ${input.supportLanguage}.`,
    `Topic: ${input.topic}.`,
    `Lesson goal: ${topicGoal}`,
    "Requirements:",
    "- teacher_explanation_english should be around 70 to 110 words",
    "- support_explanation.text should be under 60 words",
    "- examples array must contain 2 to 5 short full-sentence examples",
    "- quiz_questions array must contain exactly 5 short questions",
    "- answer_key array must contain exactly 5 entries aligned to the quiz questions",
    "- class_activity must be one short classroom activity",
    "- take_home_revision must be one short revision instruction",
    "- if the topic is not an overview topic, stay tightly focused on that one topic",
    "- do not mislabel adverbs as adjectives",
    "- do not number the array items",
    "Topic-specific guidance:",
    `- Suggested lesson title: ${topicGuide.lessonTitle}`,
    `- Teacher explanation style: ${topicGuide.teacherExplanation}`,
    `- Support explanation style: ${topicGuide.supportExplanation}`,
    `- Suggested examples: ${topicGuide.examples.join(" | ")}`,
    `- Suggested class activity: ${topicGuide.classActivity}`,
    `- Suggested quiz questions: ${topicGuide.quizQuestions.join(" | ")}`,
    `- Suggested answer key: ${topicGuide.answerKey.join(" | ")}`,
    `- Suggested take-home revision: ${topicGuide.takeHomeRevision}`,
    "Efik grounding hints:",
    glossaryHints,
    "Classroom phrase hints:",
    phraseHints
  ].join("\n");

  return { system, prompt };
}

function parseAndNormalizeLesson(rawText, input, providerName) {
  let parsed = null;

  try {
    parsed = JSON.parse(rawText);
  } catch {
    const extracted = extractFirstJsonObject(rawText);
    if (extracted) {
      parsed = JSON.parse(extracted);
    }
  }

  if (!parsed || typeof parsed !== "object") {
    return buildFallbackLesson(input, `Provider ${providerName} returned malformed JSON.`);
  }

  const normalized = normalizeLesson(parsed, input);

  if (!isValidLesson(normalized)) {
    return buildFallbackLesson(input, `Provider ${providerName} returned incomplete lesson data.`);
  }

  return normalized;
}

function normalizeLesson(candidate, input) {
  const topicGuide = getTopicGuide(input.topic);
  const topic = ensureString(candidate.topic, input.topic);
  const classLevel = ensureString(candidate.class_level, input.classLevel);
  const subject = "English";
  const supportLanguage = ensureString(candidate.support_language, input.supportLanguage);
  const supportExplanation = candidate.support_explanation && typeof candidate.support_explanation === "object"
    ? candidate.support_explanation
    : {};

  return {
    id: `lesson-${Date.now()}`,
    lesson_title: ensureString(candidate.lesson_title, topicGuide.lessonTitle),
    class_level: classLevel,
    subject,
    support_language: supportLanguage,
    topic,
    learning_objective: ensureString(
      candidate.learning_objective,
      topicPack.topics.find((item) => item.title.toLowerCase() === topic.toLowerCase())?.goal ??
        `Help learners understand ${topic.toLowerCase()} and use it in simple English examples.`
    ),
    teacher_explanation_english: preferTopicGuideText(
      candidate.teacher_explanation_english,
      topicGuide.teacherExplanation
    ),
    support_explanation: {
      language: ensureString(supportExplanation.language, supportLanguage),
      text: preferTopicGuideText(
        supportExplanation.text,
        topicGuide.supportExplanation,
        { maxWords: 60 }
      )
    },
    examples: normalizeStringArray(candidate.examples, 2, 5, topicGuide.examples),
    class_activity: ensureString(
      candidate.class_activity,
      topicGuide.classActivity
    ),
    quiz_questions: normalizeStringArray(candidate.quiz_questions, 5, 5, topicGuide.quizQuestions),
    answer_key: normalizeAnswerKey(candidate.answer_key, topicGuide.answerKey),
    take_home_revision: ensureString(
      candidate.take_home_revision,
      topicGuide.takeHomeRevision
    ),
    created_at: new Date().toISOString()
  };
}

function isValidLesson(lesson) {
  return (
    typeof lesson.lesson_title === "string" &&
    typeof lesson.class_level === "string" &&
    lesson.subject === "English" &&
    typeof lesson.support_language === "string" &&
    typeof lesson.topic === "string" &&
    typeof lesson.learning_objective === "string" &&
    typeof lesson.teacher_explanation_english === "string" &&
    typeof lesson.support_explanation?.language === "string" &&
    typeof lesson.support_explanation?.text === "string" &&
    Array.isArray(lesson.examples) &&
    lesson.examples.length >= 2 &&
    Array.isArray(lesson.quiz_questions) &&
    lesson.quiz_questions.length === 5 &&
    Array.isArray(lesson.answer_key) &&
    lesson.answer_key.length === 5 &&
    typeof lesson.class_activity === "string" &&
    typeof lesson.take_home_revision === "string"
  );
}

function buildFallbackLesson(input, reason) {
  const topicGuide = getTopicGuide(input.topic);
  return {
    id: `lesson-${Date.now()}`,
    lesson_title: topicGuide.lessonTitle,
    class_level: input.classLevel,
    subject: "English",
    support_language: input.supportLanguage,
    topic: input.topic,
    learning_objective:
      topicPack.topics.find((item) => item.title.toLowerCase() === input.topic.toLowerCase())?.goal ??
      `Help learners understand ${input.topic.toLowerCase()} and use it in short classroom examples.`,
    teacher_explanation_english: `${topicGuide.teacherExplanation} This lesson used the safe fallback path because: ${reason}`,
    support_explanation: {
      language: input.supportLanguage,
      text: topicGuide.supportExplanation
    },
    examples: topicGuide.examples,
    class_activity: topicGuide.classActivity,
    quiz_questions: topicGuide.quizQuestions,
    answer_key: topicGuide.answerKey,
    take_home_revision: topicGuide.takeHomeRevision,
    created_at: new Date().toISOString()
  };
}

function validateRequestBody(body) {
  if (!body || typeof body !== "object") {
    throw new Error("Request body must be a JSON object.");
  }

  const classLevel = ensureString(body.classLevel, "").trim();
  const subject = ensureString(body.subject, "").trim();
  const supportLanguage = ensureString(body.supportLanguage, "").trim();
  const topic = ensureString(body.topic, "").trim();

  if (!classLevel || !subject || !supportLanguage || !topic) {
    throw new Error("classLevel, subject, supportLanguage, and topic are required.");
  }

  return {
    classLevel,
    subject,
    supportLanguage,
    topic
  };
}

async function readJson(filePath) {
  const raw = await readFile(filePath, "utf8");
  return JSON.parse(raw);
}

async function readJsonBody(req) {
  const chunks = [];
  for await (const chunk of req) {
    chunks.push(chunk);
  }

  const rawBody = Buffer.concat(chunks).toString("utf8");

  if (!rawBody) {
    return {};
  }

  return JSON.parse(rawBody);
}

function ensureString(value, fallback) {
  return typeof value === "string" && value.trim() ? value.trim() : fallback;
}

function normalizeStringArray(value, minItems, maxItems, fallback) {
  if (!Array.isArray(value)) {
    return fallback.slice(0, maxItems);
  }

  const cleaned = value
    .filter((item) => typeof item === "string")
    .map((item) => sanitizeListText(item))
    .filter(Boolean)
    .slice(0, maxItems);

  if (cleaned.length < minItems) {
    return fallback.slice(0, maxItems);
  }

  return cleaned;
}

function normalizeAnswerKey(value, fallback) {
  const cleaned = normalizeStringArray(value, 5, 5, fallback);
  const genericCount = cleaned.filter((item) => /accept any|accept the correct/i.test(item)).length;
  return genericCount >= 3 ? fallback : cleaned;
}

function sanitizeListText(value) {
  return value.replace(/^\s*\d+[\.\)]\s*/, "").trim();
}

function preferTopicGuideText(value, fallback, options = {}) {
  const text = ensureString(value, "").trim();

  if (!text) {
    return fallback;
  }

  if (/should be explained with|safe fallback path|generic|placeholder/i.test(text)) {
    return fallback;
  }

  if (options.maxWords && countWords(text) > options.maxWords) {
    return fallback;
  }

  return text;
}

function countWords(text) {
  return text.trim().split(/\s+/).filter(Boolean).length;
}

function getTopicGuide(topic) {
  const key = normalizeTopicKey(topic);
  return TOPIC_GUIDES[key] ?? buildDefaultTopicGuide(topic);
}

function normalizeTopicKey(topic) {
  return topic.trim().toLowerCase().replace(/\s+/g, " ");
}

function buildDefaultTopicGuide(topic) {
  return {
    lessonTitle: `${topic} Lesson`,
    teacherExplanation: `${topic} should be taught with one short definition, two familiar examples, and one quick class check for understanding.`,
    supportExplanation: `Use a short Efik support note to explain ${topic.toLowerCase()} with familiar examples, then return to simple English practice.`,
    examples: [
      `Give one simple example of ${topic.toLowerCase()}.`,
      `Write one short sentence that uses ${topic.toLowerCase()}.`
    ],
    classActivity:
      "Write two examples on the board, ask pupils to identify the target pattern, then let pairs create one example each.",
    quizQuestions: [
      `What is ${topic.toLowerCase()}?`,
      `Give one example of ${topic.toLowerCase()}.`,
      `Use ${topic.toLowerCase()} in a short sentence.`,
      `Why is ${topic.toLowerCase()} useful in English?`,
      "Write one more example from class."
    ],
    answerKey: [
      `${topic} is defined in the lesson explanation.`,
      "Accept any correct example.",
      "Accept any correct sentence.",
      "It helps learners use English correctly.",
      "Accept any correct classroom example."
    ],
    takeHomeRevision: `Write two new examples of ${topic.toLowerCase()} at home and explain them aloud.`
  };
}

function extractFirstJsonObject(text) {
  if (typeof text !== "string") {
    return null;
  }

  const start = text.indexOf("{");
  const end = text.lastIndexOf("}");

  if (start === -1 || end === -1 || end <= start) {
    return null;
  }

  return text.slice(start, end + 1);
}

function setCorsHeaders(res) {
  res.setHeader("Access-Control-Allow-Origin", "*");
  res.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
  res.setHeader("Access-Control-Allow-Headers", "Content-Type");
}

function sendJson(res, statusCode, payload) {
  res.writeHead(statusCode, {
    "Content-Type": "application/json; charset=utf-8"
  });
  res.end(JSON.stringify(payload));
}
