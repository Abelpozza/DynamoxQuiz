package com.abel.dynamoxquiz.presentation.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun QuizScreen(
    viewModel: QuizViewModel
) {

    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        verticalArrangement = Arrangement.Center,

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        when {

            uiState.isLoading -> {

                CircularProgressIndicator()
            }

            uiState.error != null -> {

                Text(
                    text = uiState.error ?: "Unknown error",

                    color = MaterialTheme
                        .colorScheme
                        .error
                )
            }

            uiState.question != null -> {

                val question = uiState.question

                Text(
                    text = question?.statement ?: ""
                )
            }
        }
    }
}