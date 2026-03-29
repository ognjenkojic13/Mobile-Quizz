package raf.rs.rma.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import raf.rs.rma.breeds.db.BreedDao
import raf.rs.rma.breeds.db.BreedEntity
import raf.rs.rma.breeds.db.Converters
import raf.rs.rma.breeds.db.ImageDao
import raf.rs.rma.breeds.db.ImageEntity
import raf.rs.rma.quiz.db.QuizResult
import raf.rs.rma.quiz.db.QuizResultDao
import raf.rs.rma.users.db.User
import raf.rs.rma.users.db.UserDao

@Database(
    entities = [
        User::class,
        BreedEntity::class,
        ImageEntity::class,
        QuizResult::class
    ],
    version = 8,
    exportSchema = true,
)

@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun breedDao(): BreedDao
    abstract fun imageDao(): ImageDao
    abstract fun quizResultDao(): QuizResultDao

}