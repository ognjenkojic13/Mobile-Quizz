package raf.rs.rma.users.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val id: Int,
    val name: String,
    val nickname: String,
    val email: String,
    val quizCount: Int = 0
)