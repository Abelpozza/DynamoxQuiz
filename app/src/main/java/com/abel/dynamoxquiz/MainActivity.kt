package com.abel.dynamoxquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.abel.dynamoxquiz.di.RepositoryModule
import com.abel.dynamoxquiz.presentation.quiz.QuizScreen
import com.abel.dynamoxquiz.presentation.quiz.QuizViewModel
import com.abel.dynamoxquiz.presentation.quiz.QuizViewModelFactory
import com.abel.dynamoxquiz.ui.theme.DynamoxQuizTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val repository =
            RepositoryModule.provideQuizRepository()

        val factory =
            QuizViewModelFactory(repository)

        val viewModel = ViewModelProvider(
            this,
            factory
        )[QuizViewModel::class.java]

        setContent {

            DynamoxQuizTheme {

                QuizScreen(
                    viewModel = viewModel
                )
            }
        }
    }
}