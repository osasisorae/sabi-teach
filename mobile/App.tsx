import { StatusBar } from "expo-status-bar";
import { useEffect, useState } from "react";
import {
  ActivityIndicator,
  Pressable,
  SafeAreaView,
  ScrollView,
  StyleSheet,
  Text,
  TextInput,
  View
} from "react-native";

import { CLASS_LEVEL, PRIMARY_5_ENGLISH_TOPICS, SUBJECT } from "./src/content/topics";
import { SUPPORT_LANGUAGE } from "./src/content/efik";
import { generateLesson, getGenerationMode } from "./src/lib/generation";
import { loadSavedLessons, saveLesson } from "./src/lib/storage";
import type { Lesson } from "./src/types/lesson";

type Screen = "home" | "generate" | "saved" | "detail";
type NavTab = "home" | "generate" | "saved";

export default function App() {
  const [screen, setScreen] = useState<Screen>("generate");
  const [topic, setTopic] = useState("Nouns");
  const [supportLanguage] = useState(SUPPORT_LANGUAGE);
  const [currentLesson, setCurrentLesson] = useState<Lesson | null>(null);
  const [selectedLesson, setSelectedLesson] = useState<Lesson | null>(null);
  const [savedLessons, setSavedLessons] = useState<Lesson[]>([]);
  const [isGenerating, setIsGenerating] = useState(false);
  const [isSaving, setIsSaving] = useState(false);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  useEffect(() => {
    void hydrateSavedLessons();
  }, []);

  async function hydrateSavedLessons() {
    const lessons = await loadSavedLessons();
    setSavedLessons(lessons);
  }

  const activeTab: NavTab = screen === "detail" ? "saved" : screen;

  async function handleGenerateLesson() {
    setIsGenerating(true);
    setErrorMessage(null);
    try {
      const lesson = await generateLesson({
        classLevel: CLASS_LEVEL,
        subject: SUBJECT,
        topic,
        supportLanguage
      });
      setCurrentLesson(lesson);
    } catch (error) {
      const message = error instanceof Error ? error.message : "Lesson generation failed.";
      setErrorMessage(message);
    } finally {
      setIsGenerating(false);
    }
  }

  async function handleSaveLesson() {
    if (!currentLesson) {
      return;
    }

    setIsSaving(true);
    try {
      const next = await saveLesson(currentLesson);
      setSavedLessons(next);
      setSelectedLesson(currentLesson);
      setScreen("detail");
    } finally {
      setIsSaving(false);
    }
  }

  const content =
    screen === "home" ? (
      <HomeScreen
        onGenerate={() => setScreen("generate")}
        onSaved={() => setScreen("saved")}
        savedCount={savedLessons.length}
      />
    ) : screen === "generate" ? (
      <GenerateScreen
        topic={topic}
        onTopicChange={setTopic}
        onGenerate={handleGenerateLesson}
        isGenerating={isGenerating}
        currentLesson={currentLesson}
        onSaveLesson={handleSaveLesson}
        isSaving={isSaving}
        classLevel={CLASS_LEVEL}
        subject={SUBJECT}
        supportLanguage={supportLanguage}
        generationMode={getGenerationMode()}
        errorMessage={errorMessage}
      />
    ) : screen === "saved" ? (
      <SavedLessonsScreen
        savedLessons={savedLessons}
        onOpenLesson={(lesson) => {
          setSelectedLesson(lesson);
          setScreen("detail");
        }}
      />
    ) : (
      <LessonDetailScreen
        lesson={selectedLesson}
        onBack={() => setScreen("saved")}
      />
    );

  return (
    <SafeAreaView style={styles.safeArea}>
      <StatusBar style="dark" />
      <View style={styles.appShell}>
        <View style={styles.header}>
          <View>
            <Text style={styles.brand}>SabiTeach</Text>
            <Text style={styles.subbrand}>Offline teacher copilot for low-connectivity classrooms</Text>
          </View>
          <View style={styles.badge}>
            <Text style={styles.badgeText}>Primary 5 English + Efik</Text>
          </View>
        </View>
        <AppTabs activeTab={activeTab} onChange={(tab) => setScreen(tab)} />
        {content}
      </View>
    </SafeAreaView>
  );
}

function AppTabs(props: {
  activeTab: NavTab;
  onChange: (tab: NavTab) => void;
}) {
  return (
    <View style={styles.tabsWrap}>
      <View style={styles.tabsBar}>
        <TabButton
          label="Home"
          isActive={props.activeTab === "home"}
          onPress={() => props.onChange("home")}
        />
        <TabButton
          label="Generate"
          isActive={props.activeTab === "generate"}
          onPress={() => props.onChange("generate")}
        />
        <TabButton
          label="Saved"
          isActive={props.activeTab === "saved"}
          onPress={() => props.onChange("saved")}
        />
      </View>
    </View>
  );
}

function HomeScreen(props: {
  onGenerate: () => void;
  onSaved: () => void;
  savedCount: number;
}) {
  return (
    <ScrollView contentContainerStyle={styles.page}>
      <View style={styles.heroCard}>
        <Text style={styles.heroEyebrow}>Future of Education</Text>
        <Text style={styles.heroTitle}>Teach English with local-language support, even offline.</Text>
        <Text style={styles.heroBody}>
          One Android phone. One teacher. One classroom-ready lesson plan with quiz, answer key,
          and revision notes.
        </Text>
        <View style={styles.heroActions}>
          <ActionButton label="Generate Lesson" onPress={props.onGenerate} variant="primary" />
          <ActionButton label={`Saved Lessons (${props.savedCount})`} onPress={props.onSaved} variant="secondary" />
        </View>
      </View>

      <View style={styles.grid}>
        <InfoCard
          title="Locked Slice"
          body="Primary 5 English with Efik support language."
        />
        <InfoCard
          title="Offline Ready"
          body="Saved lessons reopen on the phone without internet. New lesson generation currently uses local Ollama on your computer."
        />
        <InfoCard
          title="Structured Output"
          body="Every lesson uses the same schema for teacher trust and faster review."
        />
      </View>
    </ScrollView>
  );
}

function GenerateScreen(props: {
  topic: string;
  onTopicChange: (value: string) => void;
  onGenerate: () => void;
  isGenerating: boolean;
  currentLesson: Lesson | null;
  onSaveLesson: () => void;
  isSaving: boolean;
  classLevel: string;
  subject: string;
  supportLanguage: string;
  generationMode: string;
  errorMessage: string | null;
}) {
  return (
    <ScrollView contentContainerStyle={styles.page}>
      <View style={styles.panel}>
        <Text style={styles.sectionTitle}>Generate Lesson</Text>
        <Text style={styles.supportingText}>
          Create one structured lesson for a real classroom using Gemma 4 through your local Ollama setup.
        </Text>
        <View style={styles.metaRow}>
          <Pill label={props.classLevel} />
          <Pill label={props.subject} />
          <Pill label={props.supportLanguage} />
          <Pill label={`Mode: ${props.generationMode}`} />
        </View>

        <Text style={styles.label}>Topic</Text>
        <TextInput
          value={props.topic}
          onChangeText={props.onTopicChange}
          placeholder="Enter a topic, for example Nouns"
          placeholderTextColor="#7f8a92"
          style={styles.input}
        />

        <Text style={styles.label}>Starter Topics</Text>
        <View style={styles.chipsWrap}>
          {PRIMARY_5_ENGLISH_TOPICS.map((item) => (
            <TopicChip
              key={item.id}
              label={item.title}
              isActive={item.title === props.topic}
              onPress={() => props.onTopicChange(item.title)}
            />
          ))}
        </View>

        <View style={styles.primaryActionRow}>
          <ActionButton
            label={props.isGenerating ? "Generating..." : "Generate"}
            onPress={props.onGenerate}
            variant="primary"
            disabled={props.isGenerating}
            stretch
          />
        </View>
      </View>

      {props.isGenerating ? (
        <View style={styles.loaderCard}>
          <ActivityIndicator color="#1f7a59" />
          <Text style={styles.loaderText}>Generating lesson with Gemma 4 through your local Ollama setup.</Text>
          <Text style={styles.loaderSubtext}>
            First runs can take longer while the model warms up on your machine.
          </Text>
        </View>
      ) : null}

      {props.errorMessage ? (
        <View style={styles.errorCard}>
          <Text style={styles.errorTitle}>Generation Error</Text>
          <Text style={styles.errorBody}>{props.errorMessage}</Text>
        </View>
      ) : null}

      {props.currentLesson ? (
        <View style={styles.resultCard}>
          <View style={styles.resultHeader}>
            <Text style={styles.resultEyebrow}>Generated Lesson</Text>
            <Text style={styles.resultHint}>Review, then save this lesson for offline access on the phone.</Text>
          </View>
          <Text style={styles.sectionTitle}>{props.currentLesson.lesson_title}</Text>
          <Section title="Learning Objective" body={props.currentLesson.learning_objective} />
          <Section title="Teacher Explanation (English)" body={props.currentLesson.teacher_explanation_english} />
          <Section title={`Support Explanation (${props.currentLesson.support_explanation.language})`} body={props.currentLesson.support_explanation.text} />
          <ListSection title="Examples" items={props.currentLesson.examples} />
          <Section title="Class Activity" body={props.currentLesson.class_activity} />
          <ListSection title="Quiz Questions" items={props.currentLesson.quiz_questions} />
          <ListSection title="Answer Key" items={props.currentLesson.answer_key} />
          <Section title="Take-Home Revision" body={props.currentLesson.take_home_revision} />

          <View style={styles.primaryActionRow}>
            <ActionButton
              label={props.isSaving ? "Saving..." : "Save Lesson"}
              onPress={props.onSaveLesson}
              variant="primary"
              disabled={props.isSaving}
              stretch
            />
          </View>
        </View>
      ) : null}
    </ScrollView>
  );
}

function SavedLessonsScreen(props: {
  savedLessons: Lesson[];
  onOpenLesson: (lesson: Lesson) => void;
}) {
  return (
    <ScrollView contentContainerStyle={styles.page}>
      <View style={styles.panel}>
        <Text style={styles.sectionTitle}>Saved Lessons</Text>
        <Text style={styles.supportingText}>
          Open any saved lesson stored on this phone. Saved lessons remain available without internet.
        </Text>
      </View>

      {props.savedLessons.length === 0 ? (
        <View style={styles.emptyCard}>
          <Text style={styles.emptyTitle}>No lessons saved yet.</Text>
          <Text style={styles.supportingText}>Generate one lesson first, then save it for offline access.</Text>
        </View>
      ) : (
        props.savedLessons.map((lesson) => (
          <Pressable key={lesson.id} style={styles.savedCard} onPress={() => props.onOpenLesson(lesson)}>
            <Text style={styles.savedTitle}>{lesson.lesson_title}</Text>
            <Text style={styles.savedMeta}>
              {lesson.class_level} • {lesson.subject} • {lesson.support_language}
            </Text>
            <Text style={styles.savedTopic}>{lesson.topic}</Text>
          </Pressable>
        ))
      )}
    </ScrollView>
  );
}

function LessonDetailScreen(props: {
  lesson: Lesson | null;
  onBack: () => void;
}) {
  if (!props.lesson) {
    return (
      <View style={styles.page}>
        <Text style={styles.emptyTitle}>Lesson not found.</Text>
      </View>
    );
  }

  return (
    <ScrollView contentContainerStyle={styles.page}>
      <View style={styles.panel}>
        <Text style={styles.sectionTitle}>Saved Lesson</Text>
        <Text style={styles.supportingText}>
          This lesson was saved on this phone and can be opened again without internet.
        </Text>
        <View style={styles.heroActions}>
          <ActionButton label="Back to Saved" onPress={props.onBack} variant="secondary" />
        </View>
      </View>

      <View style={styles.resultCard}>
        <Text style={styles.sectionTitle}>{props.lesson.lesson_title}</Text>
        <Section title="Learning Objective" body={props.lesson.learning_objective} />
        <Section title="Teacher Explanation (English)" body={props.lesson.teacher_explanation_english} />
        <Section title={`Support Explanation (${props.lesson.support_explanation.language})`} body={props.lesson.support_explanation.text} />
        <ListSection title="Examples" items={props.lesson.examples} />
        <Section title="Class Activity" body={props.lesson.class_activity} />
        <ListSection title="Quiz Questions" items={props.lesson.quiz_questions} />
        <ListSection title="Answer Key" items={props.lesson.answer_key} />
        <Section title="Take-Home Revision" body={props.lesson.take_home_revision} />
      </View>
    </ScrollView>
  );
}

function ActionButton(props: {
  label: string;
  onPress: () => void;
  variant: "primary" | "secondary";
  disabled?: boolean;
  stretch?: boolean;
}) {
  return (
    <Pressable
      onPress={props.onPress}
      disabled={props.disabled}
      style={[
        styles.button,
        props.variant === "primary" ? styles.buttonPrimary : styles.buttonSecondary,
        props.disabled ? styles.buttonDisabled : null,
        props.stretch ? styles.buttonStretch : null
      ]}
    >
      <Text style={props.variant === "primary" ? styles.buttonPrimaryText : styles.buttonSecondaryText}>
        {props.label}
      </Text>
    </Pressable>
  );
}

function InfoCard(props: { title: string; body: string }) {
  return (
    <View style={styles.infoCard}>
      <Text style={styles.infoTitle}>{props.title}</Text>
      <Text style={styles.infoBody}>{props.body}</Text>
    </View>
  );
}

function Section(props: { title: string; body: string }) {
  return (
    <View style={styles.sectionBlock}>
      <Text style={styles.blockTitle}>{props.title}</Text>
      <Text style={styles.blockBody}>{props.body}</Text>
    </View>
  );
}

function ListSection(props: { title: string; items: string[] }) {
  return (
    <View style={styles.sectionBlock}>
      <Text style={styles.blockTitle}>{props.title}</Text>
      {props.items.map((item, index) => (
        <Text key={`${props.title}-${index}`} style={styles.listItem}>
          {index + 1}. {cleanListItem(item)}
        </Text>
      ))}
    </View>
  );
}

function cleanListItem(item: string): string {
  return item.replace(/^\s*\d+[\.\)]\s*/, "").trim();
}

function TopicChip(props: {
  label: string;
  isActive: boolean;
  onPress: () => void;
}) {
  return (
    <Pressable
      onPress={props.onPress}
      style={[styles.chip, props.isActive ? styles.chipActive : null]}
    >
      <Text style={[styles.chipText, props.isActive ? styles.chipTextActive : null]}>{props.label}</Text>
    </Pressable>
  );
}

function Pill(props: { label: string }) {
  return (
    <View style={styles.pill}>
      <Text style={styles.pillText}>{props.label}</Text>
    </View>
  );
}

function TabButton(props: {
  label: string;
  isActive: boolean;
  onPress: () => void;
}) {
  return (
    <Pressable
      onPress={props.onPress}
      style={[styles.tabButton, props.isActive ? styles.tabButtonActive : null]}
    >
      <Text style={[styles.tabButtonText, props.isActive ? styles.tabButtonTextActive : null]}>
        {props.label}
      </Text>
    </Pressable>
  );
}

const styles = StyleSheet.create({
  safeArea: {
    flex: 1,
    backgroundColor: "#f2ede2"
  },
  appShell: {
    flex: 1,
    backgroundColor: "#f2ede2"
  },
  header: {
    paddingHorizontal: 20,
    paddingTop: 14,
    paddingBottom: 10,
    borderBottomWidth: 1,
    borderBottomColor: "#d8cfbf",
    backgroundColor: "#f8f4ea",
    gap: 8
  },
  brand: {
    fontSize: 30,
    fontWeight: "800",
    color: "#183d33"
  },
  subbrand: {
    fontSize: 14,
    lineHeight: 20,
    color: "#59656b",
    maxWidth: 340
  },
  badge: {
    alignSelf: "flex-start",
    backgroundColor: "#dceede",
    borderRadius: 999,
    paddingHorizontal: 12,
    paddingVertical: 7
  },
  badgeText: {
    color: "#1f5f46",
    fontWeight: "700",
    fontSize: 12
  },
  tabsWrap: {
    paddingHorizontal: 20,
    paddingTop: 12,
    paddingBottom: 4,
    backgroundColor: "#f2ede2"
  },
  tabsBar: {
    flexDirection: "row",
    gap: 10
  },
  tabButton: {
    flex: 1,
    backgroundColor: "#f7f2e8",
    borderWidth: 1,
    borderColor: "#d7ccbb",
    borderRadius: 20,
    paddingVertical: 12,
    alignItems: "center"
  },
  tabButtonActive: {
    backgroundColor: "#183d33",
    borderColor: "#183d33"
  },
  tabButtonText: {
    color: "#516068",
    fontWeight: "700"
  },
  tabButtonTextActive: {
    color: "#f6f0e4"
  },
  page: {
    paddingHorizontal: 20,
    paddingTop: 12,
    paddingBottom: 24,
    gap: 18
  },
  heroCard: {
    backgroundColor: "#183d33",
    borderRadius: 30,
    padding: 24,
    gap: 14
  },
  heroEyebrow: {
    color: "#d6f0df",
    fontSize: 12,
    fontWeight: "700",
    letterSpacing: 1
  },
  heroTitle: {
    color: "#ffffff",
    fontSize: 31,
    lineHeight: 38,
    fontWeight: "800"
  },
  heroBody: {
    color: "#d4dfda",
    fontSize: 15,
    lineHeight: 22
  },
  heroActions: {
    flexDirection: "row",
    flexWrap: "wrap",
    gap: 10,
    marginTop: 6
  },
  primaryActionRow: {
    flexDirection: "row",
    gap: 10,
    marginTop: 8
  },
  button: {
    borderRadius: 999,
    paddingHorizontal: 18,
    paddingVertical: 13,
    alignItems: "center",
    justifyContent: "center"
  },
  buttonPrimary: {
    backgroundColor: "#f0b35a"
  },
  buttonSecondary: {
    backgroundColor: "#f7f2e8",
    borderWidth: 1,
    borderColor: "#d7ccbb"
  },
  buttonDisabled: {
    opacity: 0.6
  },
  buttonStretch: {
    flex: 1
  },
  buttonPrimaryText: {
    color: "#1d170f",
    fontWeight: "800"
  },
  buttonSecondaryText: {
    color: "#25463d",
    fontWeight: "700"
  },
  grid: {
    gap: 12
  },
  infoCard: {
    backgroundColor: "#fcfaf4",
    borderWidth: 1,
    borderColor: "#ddd2c1",
    borderRadius: 22,
    padding: 18,
    gap: 8
  },
  infoTitle: {
    fontSize: 16,
    fontWeight: "800",
    color: "#183d33"
  },
  infoBody: {
    fontSize: 14,
    lineHeight: 20,
    color: "#57646c"
  },
  panel: {
    backgroundColor: "#fcfaf4",
    borderWidth: 1,
    borderColor: "#ddd2c1",
    borderRadius: 28,
    padding: 22,
    gap: 14
  },
  sectionTitle: {
    fontSize: 23,
    lineHeight: 31,
    fontWeight: "800",
    color: "#183d33"
  },
  metaRow: {
    flexDirection: "row",
    flexWrap: "wrap",
    gap: 10
  },
  pill: {
    backgroundColor: "#e5efe6",
    borderRadius: 999,
    paddingHorizontal: 12,
    paddingVertical: 8
  },
  pillText: {
    fontWeight: "700",
    color: "#1c6147"
  },
  label: {
    fontSize: 13,
    fontWeight: "700",
    color: "#33444c"
  },
  input: {
    borderWidth: 1,
    borderColor: "#d7ccbb",
    borderRadius: 22,
    backgroundColor: "#ffffff",
    paddingHorizontal: 18,
    paddingVertical: 16,
    color: "#1b2328",
    fontSize: 16,
    lineHeight: 22
  },
  chipsWrap: {
    flexDirection: "row",
    flexWrap: "wrap",
    gap: 10
  },
  chip: {
    backgroundColor: "#efe6d6",
    borderRadius: 999,
    paddingHorizontal: 14,
    paddingVertical: 10
  },
  chipActive: {
    backgroundColor: "#183d33"
  },
  chipText: {
    color: "#654a18",
    fontWeight: "700"
  },
  chipTextActive: {
    color: "#f6f0e4"
  },
  loaderCard: {
    backgroundColor: "#fcfaf4",
    borderWidth: 1,
    borderColor: "#ddd2c1",
    borderRadius: 28,
    padding: 22,
    gap: 12,
    alignItems: "center"
  },
  errorCard: {
    backgroundColor: "#fff1ef",
    borderWidth: 1,
    borderColor: "#e7b8b0",
    borderRadius: 28,
    padding: 20,
    gap: 8
  },
  errorTitle: {
    color: "#8a1f12",
    fontSize: 16,
    fontWeight: "800"
  },
  errorBody: {
    color: "#6d2e25",
    lineHeight: 20
  },
  loaderText: {
    textAlign: "center",
    color: "#536168",
    lineHeight: 20
  },
  loaderSubtext: {
    textAlign: "center",
    color: "#6b787f",
    lineHeight: 20,
    fontSize: 13
  },
  resultCard: {
    backgroundColor: "#fffdf7",
    borderWidth: 1,
    borderColor: "#ddd2c1",
    borderRadius: 30,
    padding: 22,
    gap: 18
  },
  resultHeader: {
    gap: 6,
    paddingBottom: 2,
    borderBottomWidth: 1,
    borderBottomColor: "#ece1cf"
  },
  resultEyebrow: {
    fontSize: 12,
    letterSpacing: 1,
    fontWeight: "800",
    color: "#8d650f"
  },
  resultHint: {
    color: "#647279",
    lineHeight: 20,
    fontSize: 13
  },
  sectionBlock: {
    gap: 8
  },
  blockTitle: {
    fontSize: 16,
    fontWeight: "800",
    color: "#183d33"
  },
  blockBody: {
    fontSize: 14,
    lineHeight: 24,
    color: "#435259"
  },
  listItem: {
    fontSize: 14,
    lineHeight: 24,
    color: "#435259"
  },
  emptyCard: {
    backgroundColor: "#fffdf7",
    borderWidth: 1,
    borderColor: "#ddd2c1",
    borderRadius: 28,
    padding: 22,
    gap: 10
  },
  emptyTitle: {
    fontSize: 18,
    fontWeight: "800",
    color: "#183d33"
  },
  supportingText: {
    fontSize: 14,
    lineHeight: 20,
    color: "#5c686f"
  },
  savedCard: {
    backgroundColor: "#fffdf7",
    borderWidth: 1,
    borderColor: "#ddd2c1",
    borderRadius: 24,
    padding: 18,
    gap: 8
  },
  savedTitle: {
    fontSize: 17,
    fontWeight: "800",
    color: "#183d33"
  },
  savedMeta: {
    color: "#627078",
    fontSize: 13
  },
  savedTopic: {
    color: "#8d650f",
    fontWeight: "700"
  }
});
