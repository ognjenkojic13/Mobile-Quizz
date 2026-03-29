package raf.rs.rma.quiz.leaderboard.api

import kotlinx.serialization.Serializable
import raf.rs.rma.quiz.leaderboard.QuizResultApiModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LeaderboardApi {

    @GET("leaderboard")
    suspend fun getLeaderboard(@Query("category") category: Int): List<QuizResultApiModel>

    @POST("leaderboard")
    suspend fun postQuizResult(@Body quizResult: QuizResultApiModel): QuizResultResponse

}

@Serializable
data class QuizResultResponse(
    val result: QuizResultApiModel,
    val ranking: Int
)
