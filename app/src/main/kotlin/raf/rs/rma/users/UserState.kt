package raf.rs.rma.users

import raf.rs.rma.quiz.db.QuizResult

data class UserState(
    val currentUser: UserUiModel? = null,
    val loading: Boolean = true,
    val updating: Boolean = false,
    val users: List<UserUiModel> = emptyList(),
    val quizResults: List<QuizResult> = emptyList(),
    val bestQuizResult: QuizResult? = null,
    val bestLeaderboardPosition: Int = 0,
    val quizCount: Int = 0
)