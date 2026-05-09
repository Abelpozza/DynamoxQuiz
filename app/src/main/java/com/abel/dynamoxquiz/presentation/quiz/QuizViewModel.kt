package com.abel.dynamoxquiz.presentation.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abel.dynamoxquiz.domain.repository.QuizRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuizViewModel(
    private val repository: QuizRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        QuizUiState()
    )

    val uiState: StateFlow<QuizUiState> =
        _uiState.asStateFlow()

    private val _isCorrect =
        MutableStateFlow<Boolean?>(null)

    val isCorrect: StateFlow<Boolean?> =
        _isCorrect.asStateFlow()

    init {
        loadQuestion()
    }

    fun loadQuestion() {

        viewModelScope.launch {

            _uiState.value = QuizUiState(
                isLoading = true
            )

            try {

                val question = repository.getQuestion()

                _uiState.value = QuizUiState(
                    question = question
                )

            } catch (exception: Exception) {

                _uiState.value = QuizUiState(
                    error = exception.message
                )
            }
        }
    }

    fun sendAnswer(answer: String) {

        val questionId =
            _uiState.value.question?.id ?: return

        viewModelScope.launch {

            try {

                val response =
                    repository.sendAnswer(
                        questionId = questionId,
                        answer = answer
                    )

                _isCorrect.value =
                    response.correct

            } catch (exception: Exception) {

                _uiState.value = QuizUiState(
                    error = exception.message
                )
            }
        }
    }
}