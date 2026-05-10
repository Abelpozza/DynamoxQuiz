package com.abel.dynamoxquiz.presentation.quiz

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun QuizScreen(
    viewModel: QuizViewModel
) {

    val uiState by viewModel.uiState.collectAsState()

    val isCorrectFromApi by
    viewModel.isCorrect.collectAsState()

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
                var selectedOption by remember {
                    mutableStateOf<String?>(null)
                }
                var answerChecked by remember {
                    mutableStateOf(false)
                }
                Column(
                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {
                    Text(
                        text = question?.statement ?: "",
                        style = MaterialTheme
                            .typography
                            .headlineSmall
                    )
                    Spacer(
                        modifier = Modifier
                            .padding(8.dp)
                    )
                    question?.options?.forEachIndexed { index, option ->
                        val optionLetter =
                            ('A' + index)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp)
                                .border(
                                    width = 1.dp,
                                    color = Color.Gray,
                                    shape =
                                        RoundedCornerShape(12.dp)
                                )
                                .clickable {
                                    selectedOption = option
                                }
                                .padding(16.dp),

                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected =
                                    selectedOption == option,
                                onClick = {
                                    selectedOption = option
                                }
                            )
                            Spacer(
                                modifier = Modifier
                                    .width(8.dp)
                            )
                            Text(
                                text =
                                    "$optionLetter) $option",

                                style = MaterialTheme
                                    .typography
                                    .bodyLarge,
                                fontWeight =
                                    FontWeight.Normal
                            )
                        }
                    }
                    Button(
                        onClick = {
                            if (!answerChecked) {
                                selectedOption?.let {
                                    viewModel.sendAnswer(it)
                                    answerChecked = true
                                }

                            } else {
                                selectedOption = null
                                answerChecked = false
                                viewModel.loadQuestion()
                            }
                        },
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor =
                                    Color.Black,
                                contentColor =
                                    Color.White
                            ),
                        modifier = Modifier
                            .padding(top = 24.dp)
                    ) {
                        Text(
                            if (!answerChecked)
                                "Verificar resposta"
                            else
                                "Próxima pergunta"
                        )
                    }
                    if (answerChecked) {
                        Text(
                            text =
                                if (isCorrectFromApi == true)
                                    "✅Resposta correta✅"
                                else
                                    "❌Resposta incorreta❌",
                            color =
                                if (isCorrectFromApi == true)
                                    Color(0xFF2E7D32)
                                else
                                    Color.Red,
                            modifier = Modifier
                                .padding(top = 16.dp),
                            fontWeight =
                                FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}