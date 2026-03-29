package raf.rs.rma.quiz.leaderboard

import kotlinx.serialization.Serializable

data class LeaderboardState(
    val isLoading: Boolean = false,
    val results: List<QuizResultApiModel> = emptyList(),
    val error: String? = null,
    val currentUserRank: Int? = null,
    val quizCount: Map<String, Int> = emptyMap(),
    val bestLeaderboardPosition: Int = 0,
)

@Serializable
data class QuizResultApiModel(
    val category: Int,
    val nickname: String,
    val result: Float,
    val quizCount: Int = 0
)
