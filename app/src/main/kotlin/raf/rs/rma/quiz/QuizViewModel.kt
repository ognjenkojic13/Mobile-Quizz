package raf.rs.rma.quiz

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import raf.rs.rma.breeds.repository.CatBreedRepository
import raf.rs.rma.quiz.db.QuizRepository
import raf.rs.rma.quiz.db.QuizResult
import raf.rs.rma.users.repository.UserRepository
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val repository: CatBreedRepository,
    private val quizRepository: QuizRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(QuizState())
    val state: StateFlow<QuizState> = _state.asStateFlow()

    private val timer = object : CountDownTimer(5 * 60 * 1000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            setState { copy(remainingTime = millisUntilFinished / 1000) }
        }
        override fun onFinish() {
            completeQuiz()
        }
    }

    private fun setState(reducer: QuizState.() -> QuizState) {
        _state.update(reducer)
    }

    init {
        startQuiz()
        timer.start()
        generateQuestions()
    }

    private fun generateQuestions() {
        viewModelScope.launch {
            setState { copy(isGeneratingQuestions = true) }
            val questions = withContext(Dispatchers.IO) {
                generateGuessTheFactQuestions()
            }
            setState {
                copy(
                    questions = questions,
                    isGeneratingQuestions = false
                )
            }
        }
    }

    fun startQuiz() {
        setState { copy(currentQuestionIndex = 0, score = 0F, isQuizCompleted = false) }
    }

    fun cancelQuiz() {
        setState { copy (isQuizCompleted = true, isQuizCanceled = true) }
    }

    fun submitAnswer(answer: String) {
        val currentQuestion = _state.value.questions[_state.value.currentQuestionIndex]
        val isCorrect = currentQuestion.correctAnswer == answer
        val updatedScore = if (isCorrect) _state.value.score + 1 else _state.value.score
        val nextIndex = _state.value.currentQuestionIndex + 1
        val isCompleted = nextIndex >= _state.value.questions.size

        setState {
            copy(
                currentQuestionIndex = nextIndex,
                score = updatedScore,
            )
        }

        if (isCompleted) {
            completeQuiz()
        }
    }

    private fun completeQuiz() {
        setState {
            copy(
                currentQuestionIndex = questions.size,
                score = score,
                isQuizCompleted = true
            )
        }
        saveQuizResult()
    }

    private fun calculateFinalScore(): Float {
        val bto = _state.value.score
        val mvt = 300.0
        val pvt = _state.value.remainingTime
        val ubp = bto * 2.5 * (1 + (pvt + 120.0) / mvt)
        val finalScore = String.format("%.2f", ubp.coerceAtMost(100.0).toFloat()).toFloat()

        Log.d("QuizViewModel", "BTO: $bto, MVT: $mvt, PVT: $pvt, UBP: $ubp, FinalScore: $finalScore")

        return finalScore
    }

    private fun saveQuizResult() {
        viewModelScope.launch {
            userRepository.getCurrentUser().collect { currentUser ->
                val nickname = currentUser?.nickname ?: "defaultUser"
                val finalScore = calculateFinalScore()
                setState { copy(finalScore = finalScore) }
                val quizResult = QuizResult(
                    nickname = nickname,
                    score = finalScore,
                    timestamp = System.currentTimeMillis(),
                    category = "Guess the Fact"
                )

                Log.d("QuizViewModel", "Saving quiz result: $quizResult")
                withContext(Dispatchers.IO) {
                    quizRepository.insertQuizResult(quizResult)
                }
            }
        }
    }

    private suspend fun generateGuessTheFactQuestions(): List<Question> {
        val breeds = repository.getAllBreeds()
        val questions = mutableListOf<Question>()
        val random = Random.Default

        val selectedBreeds = breeds.shuffled().take(20)

        selectedBreeds.forEach { breed ->
            when (random.nextInt(3)) {
                0 -> {
                    // Koja je rasa mačke?
                    val incorrectAnswers = breeds.filter { it.id != breed.id }.shuffled().take(3).map { it.name }
                    val options = (incorrectAnswers + breed.name).shuffled()

                    questions.add(
                        Question(
                            type = QuestionType.BREED,
                            text = "Koja je rasa mačke?",
                            imageUrl = breed.imageUrl ?: "",
                            options = options,
                            correctAnswer = breed.name
                        )
                    )
                }
                1 -> {
                    // Izbaci uljeza!
                    val correctTemperament = breed.temperament.random()
                    val incorrectTemperaments = breeds
                        .flatMap { it.temperament }
                        .filter { it != correctTemperament }
                        .shuffled()
                        .take(3)
                    val options = (incorrectTemperaments + correctTemperament).shuffled()

                    questions.add(
                        Question(
                            type = QuestionType.ODD_ONE_OUT,
                            text = "Izbaci uljeza!",
                            imageUrl = breed.imageUrl ?: "",
                            options = options,
                            correctAnswer = correctTemperament
                        )
                    )
                }
                2 -> {
                    // Koji temperament pripada zadatoj mački?
                    val correctTemperament = breed.temperament.random()
                    val incorrectTemperaments = breeds
                        .flatMap { it.temperament }
                        .filter { it != correctTemperament }
                        .shuffled()
                        .take(3)
                    val options = (incorrectTemperaments + correctTemperament).shuffled()

                    questions.add(
                        Question(
                            type = QuestionType.TEMPERAMENT,
                            text = "Koji temperament pripada zadatoj mački?",
                            imageUrl = breed.imageUrl ?: "",
                            options = options,
                            correctAnswer = correctTemperament
                        )
                    )
                }
            }
        }

        return questions
    }
}


