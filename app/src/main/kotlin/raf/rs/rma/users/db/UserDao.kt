package raf.rs.rma.users.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<User>)

    @Query("SELECT * FROM User")
    fun observeAll(): Flow<List<User>>

    @Query("SELECT * FROM User WHERE nickname = :nickname")
    fun getUserByNickname(nickname: String): Flow<User?>
}