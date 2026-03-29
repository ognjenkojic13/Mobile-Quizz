package raf.rs.rma.quiz.leaderboard

import raf.rs.rma.quiz.leaderboard.api.LeaderboardApi
import raf.rs.rma.quiz.leaderboard.api.QuizResultResponse
import javax.inject.Inject

class LeaderboardRepository @Inject constructor(
    private val leaderboardApi: LeaderboardApi
) {

    suspend fun getLeaderboard(category: Int): List<QuizResultApiModel> {
        return leaderboardApi.getLeaderboard(category)
    }

    suspend fun postQuizResult(quizResult: QuizResultApiModel): QuizResultResponse {
        return leaderboardApi.postQuizResult(quizResult)
    }

    suspend fun getLeaderboardWithQuizCount(category: Int): List<QuizResultApiModel> {
        val leaderboard = leaderboardApi.getLeaderboard(category)
        val quizCountMap = leaderboard.groupingBy { it.nickname }.eachCount()

        return leaderboard.map { result ->
            result.copy(quizCount = quizCountMap[result.nickname] ?: 0)
        }
    }
}