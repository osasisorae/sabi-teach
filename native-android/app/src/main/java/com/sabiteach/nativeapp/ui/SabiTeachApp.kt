package com.sabiteach.nativeapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.sabiteach.nativeapp.model.Lesson
import com.sabiteach.nativeapp.model.LessonMode
import com.sabiteach.nativeapp.model.LessonModeFormatter
import kotlinx.coroutines.launch

private val starterTopics = listOf(
    "Nouns",
    "Pronouns",
    "Verbs",
    "Adjectives",
    "Simple Sentence Structure",
    "Basic Tenses",
    "Parts of Speech Overview"
)

@Composable
@OptIn(ExperimentalLayoutApi::class)
fun SabiTeachApp(viewModel: SabiTeachViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    Scaffold { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F3EA))
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                HeaderCard()
            }

            item {
                Card(
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Generate Lesson",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "This native branch keeps the teacher workflow and teaching modes, but moves generation toward on-device Android.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF52616B)
                        )

                        OutlinedTextField(
                            value = uiState.topic,
                            onValueChange = viewModel::updateTopic,
                            label = { Text("Topic") },
                            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp)
                        )

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            starterTopics.forEach { topic ->
                                AssistChip(
                                    onClick = { viewModel.updateTopic(topic) },
                                    label = { Text(topic) }
                                )
                            }
                        }

                        Button(
                            onClick = { scope.launch { viewModel.generateLesson() } },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp)
                        ) {
                            Text("Generate Lesson")
                        }

                        if (uiState.isGenerating) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                CircularProgressIndicator()
                                Text("Building a structured lesson for the teacher workflow...")
                            }
                        }

                        uiState.errorMessage?.let { message ->
                            Text(
                                text = message,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            uiState.currentLesson?.let { lesson ->
                item {
                    GeneratedLessonCard(
                        lesson = lesson,
                        selectedMode = uiState.selectedMode,
                        onSelectMode = viewModel::selectMode,
                        onSave = viewModel::saveCurrentLesson
                    )
                }
            }

            if (uiState.savedLessons.isNotEmpty()) {
                item {
                    Text(
                        text = "Saved Lessons",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                items(uiState.savedLessons) { lesson ->
                    SavedLessonRow(
                        lesson = lesson,
                        onOpen = { viewModel.openSavedLesson(lesson) }
                    )
                }
            }
        }
    }
}

@Composable
private fun HeaderCard() {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFCF6))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "SabiTeach",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = "Native Android transition for true offline teaching support.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF52616B)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Tag("Primary 5")
                Tag("English")
                Tag("Efik")
            }
        }
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun GeneratedLessonCard(
    lesson: Lesson,
    selectedMode: LessonMode,
    onSelectMode: (LessonMode) -> Unit,
    onSave: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = lesson.lessonTitle,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                LessonMode.entries.forEach { mode ->
                    val active = mode == selectedMode
                    if (active) {
                        Button(
                            onClick = { onSelectMode(mode) },
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(modeLabel(mode))
                        }
                    } else {
                        OutlinedButton(
                            onClick = { onSelectMode(mode) },
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(modeLabel(mode))
                        }
                    }
                }
            }

            Surface(
                color = Color(0xFFFFFCF6),
                shape = RoundedCornerShape(20.dp)
            ) {
                Box(modifier = Modifier.padding(16.dp)) {
                    when (selectedMode) {
                        LessonMode.Teacher -> TeacherMode(lesson)
                        LessonMode.Handout -> HandoutMode(lesson)
                        LessonMode.OralQuiz -> OralQuizMode(lesson)
                        LessonMode.BoardWork -> BoardWorkMode(lesson)
                    }
                }
            }

            Button(
                onClick = onSave,
                shape = RoundedCornerShape(18.dp)
            ) {
                Text("Save Lesson")
            }
        }
    }
}

@Composable
private fun TeacherMode(lesson: Lesson) {
    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        SectionBlock("Learning Objective", lesson.learningObjective)
        SectionBlock("Teacher Explanation (English)", lesson.teacherExplanationEnglish)
        SectionBlock(
            "${lesson.supportLanguage} Support",
            LessonModeFormatter.simplifySupportText(
                lesson.supportExplanation.text,
                lesson.supportLanguage
            )
        )
        BulletBlock("Teacher Steps", LessonModeFormatter.buildTeacherSteps(lesson))
        BulletBlock("Examples", lesson.examples)
    }
}

@Composable
private fun HandoutMode(lesson: Lesson) {
    val handout = LessonModeFormatter.buildStudentHandout(lesson)
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        SectionBlock("Handout Title", handout.title)
        SectionBlock("Today", handout.intro)
        SectionBlock("Objective", handout.objective)
        BulletBlock("Examples", handout.examples)
        BulletBlock("Practice", handout.practice)
        SectionBlock("Homework", handout.homework)
    }
}

@Composable
private fun OralQuizMode(lesson: Lesson) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        LessonModeFormatter.buildOralQuiz(lesson).forEachIndexed { index, item ->
            SectionBlock(
                "Question ${index + 1}",
                "${item.prompt}\n\nExpected answer: ${item.expectedAnswer}"
            )
        }
    }
}

@Composable
private fun BoardWorkMode(lesson: Lesson) {
    val board = LessonModeFormatter.buildBoardWork(lesson)
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        SectionBlock("Board Title", board.boardTitle)
        SectionBlock("Board Objective", board.boardObjective)
        BulletBlock("Key Points", board.keyPoints)
        BulletBlock("Examples", board.examples)
        BulletBlock("Quick Task", board.quickTask)
    }
}

@Composable
private fun SavedLessonRow(lesson: Lesson, onOpen: () -> Unit) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = lesson.lessonTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${lesson.classLevel} • ${lesson.subject} • ${lesson.supportLanguage}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF52616B)
                )
            }
            TextButton(onClick = onOpen) {
                Text("Open")
            }
        }
    }
}

@Composable
private fun SectionBlock(title: String, body: String) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = body,
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF24343E)
        )
    }
}

@Composable
private fun BulletBlock(title: String, items: List<String>) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        items.forEachIndexed { index, item ->
            Text(
                text = "${index + 1}. $item",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF24343E)
            )
        }
    }
}

@Composable
private fun Tag(label: String) {
    Surface(
        color = Color(0xFFE7F3EA),
        shape = RoundedCornerShape(999.dp)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            color = Color(0xFF21543D),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

private fun modeLabel(mode: LessonMode): String {
    return when (mode) {
        LessonMode.Teacher -> "Teacher"
        LessonMode.Handout -> "Handout"
        LessonMode.OralQuiz -> "Oral Quiz"
        LessonMode.BoardWork -> "Board Work"
    }
}
