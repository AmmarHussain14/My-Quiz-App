package com.example.myquizapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myquizapp.data.Question
import com.example.myquizapp.data.QuizApiServiceImpl
import com.example.myquizapp.data.QuizRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class QuizState(
    val questions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val selectedAnswerIndex: Int? = null,
    val showResult: Boolean = false,
    val correctAnswers: Int = 0,
    val skippedQuestions: Int = 0,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val isLoading: Boolean = true,
    val error: String? = null,
    val isAnswerRevealed: Boolean = false,
    val showStreakBadge: Boolean = false
)

class QuizViewModel : ViewModel() {
    private val repository = QuizRepository(QuizApiServiceImpl())
    
    private val _quizState = MutableLiveData<QuizState>()
    val quizState: LiveData<QuizState> = _quizState

    init {
        _quizState.value = QuizState()
        loadQuestions()
    }

    fun loadQuestions() {
        viewModelScope.launch {
            _quizState.value = _quizState.value?.copy(isLoading = true, error = null) ?: QuizState(isLoading = true)
            
            repository.getQuestions().fold(
                onSuccess = { questions ->
                    _quizState.value = _quizState.value.copy(
                        questions = questions,
                        isLoading = false
                    )
                },
                onFailure = { error ->
                    _quizState.value = _quizState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Failed to load questions"
                    )
                }
            )
        }
    }

    fun selectAnswer(answerIndex: Int) {
        val currentState = _quizState.value
        if (currentState.isAnswerRevealed || currentState.showResult) return

        val currentQuestion = currentState.questions[currentState.currentQuestionIndex]
        val isCorrect = answerIndex == currentQuestion.correctOptionIndex
        
        val newCorrectAnswers = if (isCorrect) currentState.correctAnswers + 1 else currentState.correctAnswers
        val newStreak = if (isCorrect) currentState.currentStreak + 1 else 0
        val newLongestStreak = maxOf(currentState.longestStreak, newStreak)
        val shouldShowStreakBadge = newStreak == 3

        _quizState.value = currentState.copy(
            selectedAnswerIndex = answerIndex,
            isAnswerRevealed = true,
            correctAnswers = newCorrectAnswers,
            currentStreak = newStreak,
            longestStreak = newLongestStreak,
            showStreakBadge = shouldShowStreakBadge
        )

        // Auto-advance after 2 seconds
        viewModelScope.launch {
            delay(2000)
            if (!currentState.showResult) {
                nextQuestion()
            }
        }
    }

    fun skipQuestion() {
        val currentState = _quizState.value
        if (currentState.isAnswerRevealed || currentState.showResult) return

        _quizState.value = currentState.copy(
            skippedQuestions = currentState.skippedQuestions + 1,
            currentStreak = 0
        )

        nextQuestion()
    }

    private fun nextQuestion() {
        val currentState = _quizState.value
        val nextIndex = currentState.currentQuestionIndex + 1

        if (nextIndex >= currentState.questions.size) {
            // Quiz completed
            _quizState.value = currentState.copy(
                showResult = true,
                showStreakBadge = false
            )
        } else {
            _quizState.value = currentState.copy(
                currentQuestionIndex = nextIndex,
                selectedAnswerIndex = null,
                isAnswerRevealed = false,
                showStreakBadge = false
            )
        }
    }

    fun restartQuiz() {
        _quizState.value = QuizState(
            questions = _quizState.value.questions,
            isLoading = false
        )
    }

    fun dismissStreakBadge() {
        _quizState.value = _quizState.value.copy(showStreakBadge = false)
    }

    fun getCurrentQuestion(): Question? {
        return _quizState.value?.questions?.getOrNull(_quizState.value?.currentQuestionIndex ?: 0)
    }

    fun getProgress(): Float {
        val state = _quizState.value
        return if (state?.questions?.isNotEmpty() == true) {
            ((state.currentQuestionIndex + 1).toFloat() / state.questions.size)
        } else 0f
    }
    
    // Add getters for data binding
    val currentQuestionIndex: Int
        get() = _quizState.value?.currentQuestionIndex ?: 0
    
    val questions: List<Question>
        get() = _quizState.value?.questions ?: emptyList()
    
    val correctAnswers: Int
        get() = _quizState.value?.correctAnswers ?: 0
    
    val skippedQuestions: Int
        get() = _quizState.value?.skippedQuestions ?: 0
    
    val longestStreak: Int
        get() = _quizState.value?.longestStreak ?: 0
    
    val currentStreak: Int
        get() = _quizState.value?.currentStreak ?: 0
    
    val isAnswerRevealed: Boolean
        get() = _quizState.value?.isAnswerRevealed ?: false
    
    val showStreakBadge: Boolean
        get() = _quizState.value?.showStreakBadge ?: false
}
