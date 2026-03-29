package raf.rs.rma.quiz.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class QuizResult(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val score: Float,
    val nickname: String,
    val timestamp: Long,
    val category: String
)
