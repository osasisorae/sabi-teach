package com.sabiteach.nativeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.sabiteach.nativeapp.generation.AicoreLessonGenerator
import com.sabiteach.nativeapp.generation.ApiLessonGenerator
import com.sabiteach.nativeapp.generation.GeneratorMode
import com.sabiteach.nativeapp.generation.LessonGenerator
import com.sabiteach.nativeapp.generation.MockLessonGenerator
import com.sabiteach.nativeapp.storage.SharedPreferencesLessonStore
import com.sabiteach.nativeapp.ui.SabiTeachApp
import com.sabiteach.nativeapp.ui.SabiTeachViewModel
import com.sabiteach.nativeapp.ui.theme.SabiTeachTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val generator = resolveGenerator()
        val lessonStore = SharedPreferencesLessonStore(applicationContext)
        val viewModel = SabiTeachViewModel(
            generator = generator,
            lessonStore = lessonStore
        )

        setContent {
            SabiTeachTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    SabiTeachApp(viewModel = viewModel)
                }
            }
        }
    }

    private fun resolveGenerator(): LessonGenerator {
        val baseUrl = BuildConfig.SABITEACH_API_BASE_URL.trim()
        val configuredMode = BuildConfig.SABITEACH_GENERATION_MODE.trim().lowercase()

        val mode = when (configuredMode) {
            "remote" -> GeneratorMode.RemoteApi
            "aicore", "ondevice", "on-device" -> GeneratorMode.OnDevice
            "mock" -> GeneratorMode.Mock
            else -> if (baseUrl.isNotBlank()) GeneratorMode.RemoteApi else GeneratorMode.Mock
        }

        return when (mode) {
            GeneratorMode.Mock -> MockLessonGenerator()
            GeneratorMode.RemoteApi -> ApiLessonGenerator(baseUrl = baseUrl)
            GeneratorMode.OnDevice -> AicoreLessonGenerator()
        }
    }
}
