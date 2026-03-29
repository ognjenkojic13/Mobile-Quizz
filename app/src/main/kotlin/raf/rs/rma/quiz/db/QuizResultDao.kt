package raf.rs.rma.quiz.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizResultDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizResult(quizResult: QuizResult)

    @Query("SELECT * FROM QuizResult ORDER BY timestamp DESC")
    fun observeAllQuizResults(): Flow<List<QuizResult>>

    @Query("SELECT * FROM QuizResult ORDER BY score DESC LIMIT 1")
    suspend fun getBestQuizResult(): QuizResult?

    @Query("SELECT * FROM QuizResult WHERE nickname = :nickname")
    fun getResultsByNickname(nickname: String): Flow<List<QuizResult>>

}
