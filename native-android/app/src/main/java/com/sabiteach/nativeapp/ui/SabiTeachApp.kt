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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.sabiteach.nativeapp.generation.GeneratorAvailability
import com.sabiteach.nativeapp.generation.GeneratorMode
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

    LaunchedEffect(viewModel) {
        viewModel.refreshAvailability()
    }

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

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            GeneratorTag(
                                label = "Path: ${generatorModeLabel(uiState.generatorMode)}",
                                accent = Color(0xFF21543D),
                                background = Color(0xFFE7F3EA)
                            )
                            GeneratorTag(
                                label = "Status: ${availabilityLabel(uiState.availability)}",
                                accent = availabilityAccent(uiState.availability),
                                background = availabilityBackground(uiState.availability)
                            )
                        }

                        Text(
                            text = generatorStatusBody(uiState.generatorMode, uiState.availability),
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
                            enabled = uiState.availability == GeneratorAvailability.Ready ||
                                uiState.availability == GeneratorAvailability.DownloadRequired,
                            shape = RoundedCornerShape(18.dp)
                        ) {
                            Text(generateButtonLabel(uiState.generatorMode, uiState.availability))
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

            Text(
                text = "Topic: ${lesson.topic}",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF52616B)
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
                    text = "${lesson.classLevel} • ${lesson.subject} • ${lesson.supportLanguage} • ${lesson.topic}",
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
    GeneratorTag(
        label = label,
        accent = Color(0xFF21543D),
        background = Color(0xFFE7F3EA)
    )
}

@Composable
private fun GeneratorTag(label: String, accent: Color, background: Color) {
    Surface(
        color = background,
        shape = RoundedCornerShape(999.dp)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            color = accent,
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

private fun generatorModeLabel(mode: GeneratorMode): String {
    return when (mode) {
        GeneratorMode.Mock -> "Mock"
        GeneratorMode.RemoteApi -> "Remote API"
        GeneratorMode.OnDevice -> "On Device"
    }
}

private fun availabilityLabel(availability: GeneratorAvailability): String {
    return when (availability) {
        GeneratorAvailability.Ready -> "Ready"
        GeneratorAvailability.DownloadRequired -> "Download Required"
        GeneratorAvailability.Downloading -> "Downloading"
        GeneratorAvailability.Unavailable -> "Unavailable"
        GeneratorAvailability.Unsupported -> "Unsupported"
        GeneratorAvailability.Unknown -> "Checking"
    }
}

private fun generatorStatusBody(mode: GeneratorMode, availability: GeneratorAvailability): String {
    return when (mode) {
        GeneratorMode.Mock -> "Mock mode runs entirely inside the app so the teacher workflow stays testable without server setup."
        GeneratorMode.RemoteApi -> when (availability) {
            GeneratorAvailability.Ready -> "Remote API mode is active. The app will call the configured lesson server from this device."
            GeneratorAvailability.DownloadRequired -> "Remote API mode does not require on-device model downloads."
            GeneratorAvailability.Downloading -> "Remote API mode does not download on-device model assets."
            GeneratorAvailability.Unavailable -> "Remote API mode needs SABITEACH_API_BASE_URL before lesson generation can run."
            GeneratorAvailability.Unsupported -> "Remote API mode is not available in this build."
            GeneratorAvailability.Unknown -> "Remote API status is still being checked."
        }

        GeneratorMode.OnDevice -> when (availability) {
            GeneratorAvailability.Ready -> "On-device generation is available on this device."
            GeneratorAvailability.DownloadRequired -> "This device supports Gemini Nano, but the on-device model still needs to be downloaded."
            GeneratorAvailability.Downloading -> "Gemini Nano is downloading on this device now."
            GeneratorAvailability.Unavailable -> "On-device generation is unavailable on this device right now."
            GeneratorAvailability.Unsupported -> "On-device generation is not supported in this build."
            GeneratorAvailability.Unknown -> "On-device generation support is still being checked."
        }
    }
}

private fun generateButtonLabel(mode: GeneratorMode, availability: GeneratorAvailability): String {
    return if (availability == GeneratorAvailability.Ready || availability == GeneratorAvailability.DownloadRequired) {
        "Generate Lesson"
    } else {
        when (mode) {
            GeneratorMode.Mock -> "Generate Lesson"
            GeneratorMode.RemoteApi -> "Remote Generation Unavailable"
            GeneratorMode.OnDevice -> "On-Device Generation Unavailable"
        }
    }
}

private fun availabilityAccent(availability: GeneratorAvailability): Color {
    return when (availability) {
        GeneratorAvailability.Ready -> Color(0xFF21543D)
        GeneratorAvailability.DownloadRequired -> Color(0xFF7C4D00)
        GeneratorAvailability.Downloading -> Color(0xFF0B5CAD)
        GeneratorAvailability.Unknown -> Color(0xFF815B00)
        GeneratorAvailability.Unavailable -> Color(0xFF9A3412)
        GeneratorAvailability.Unsupported -> Color(0xFF8B1E3F)
    }
}

private fun availabilityBackground(availability: GeneratorAvailability): Color {
    return when (availability) {
        GeneratorAvailability.Ready -> Color(0xFFE7F3EA)
        GeneratorAvailability.DownloadRequired -> Color(0xFFF8E6BE)
        GeneratorAvailability.Downloading -> Color(0xFFDCEBFA)
        GeneratorAvailability.Unknown -> Color(0xFFF6E8BF)
        GeneratorAvailability.Unavailable -> Color(0xFFFBE3D5)
        GeneratorAvailability.Unsupported -> Color(0xFFF7D9E3)
    }
}
