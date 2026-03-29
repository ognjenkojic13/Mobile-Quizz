package raf.rs.rma.quiz.db

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import raf.rs.rma.db.AppDatabase
import javax.inject.Inject

class QuizRepository @Inject constructor(
    private val database: AppDatabase
) {

    suspend fun insertQuizResult(quizResult: QuizResult) {
        withContext(Dispatchers.IO) {
            database.quizResultDao().insertQuizResult(quizResult)
        }
    }

    fun getResultsByNickname(nickname: String): Flow<List<QuizResult>> {
        return database.quizResultDao().getResultsByNickname(nickname)
    }

}