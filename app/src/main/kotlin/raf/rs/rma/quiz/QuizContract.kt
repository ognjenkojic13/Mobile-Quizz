package raf.rs.rma.quiz

enum class QuestionType {
    BREED,
    ODD_ONE_OUT,
    TEMPERAMENT
}

data class Question(
    val type: QuestionType,
    val text: String,
    val imageUrl: String,
    val options: List<String>,
    val correctAnswer: String
)

data class QuizState(
    val questions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val score: Float = 0F,
    val isQuizCompleted: Boolean = false,
    val remainingTime: Long = 300L,
    val isGeneratingQuestions: Boolean = true,
    val finalScore: Float = 0F,
    val isQuizCanceled: Boolean = false,
    val resetRequested: Boolean = false
)