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

    private val _currentQuestion = MutableStateFlow(1)

    val currentQuestion: StateFlow<Int> = _currentQuestion.asStateFlow()

    private val _score = MutableStateFlow(0)

    val score: StateFlow<Int> = _score.asStateFlow()

    private val _quizFinished = MutableStateFlow(false)

    val quizFinished: StateFlow<Boolean> = _quizFinished.asStateFlow()

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

                println("RESPOSTA DA API -> $response")

                _isCorrect.value =
                    response.result

                if (response.result) {

                    _score.value++
                }

                if (_currentQuestion.value >= 10) {

                    _quizFinished.value = true

                } else {

                    _currentQuestion.value++
                }

            } catch (exception: Exception) {

                _uiState.value = QuizUiState(
                    error = exception.message
                )
            }
        }
    }

    fun restartQuiz() {

        _score.value = 0

        _currentQuestion.value = 1

        _quizFinished.value = false

        _isCorrect.value = null

        loadQuestion()
    }
    fun finishQuiz() {

        _quizFinished.value = true
    }
}
