package com.sabiteach.nativeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.sabiteach.nativeapp.generation.ApiLessonGenerator
import com.sabiteach.nativeapp.generation.MockLessonGenerator
import com.sabiteach.nativeapp.ui.SabiTeachApp
import com.sabiteach.nativeapp.ui.SabiTeachViewModel
import com.sabiteach.nativeapp.ui.theme.SabiTeachTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val generator = BuildConfig.SABITEACH_API_BASE_URL.trim().let { baseUrl ->
            if (baseUrl.isBlank()) {
                MockLessonGenerator()
            } else {
                ApiLessonGenerator(baseUrl = baseUrl)
            }
        }
        val viewModel = SabiTeachViewModel(generator = generator)

        setContent {
            SabiTeachTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    SabiTeachApp(viewModel = viewModel)
                }
            }
        }
    }
}
