package raf.rs.rma.breeds.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BreedDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(breeds: List<BreedEntity>)

    @Query("SELECT * FROM BreedEntity")
    fun observeAllBreeds(): Flow<List<BreedEntity>>

    @Query("SELECT * FROM BreedEntity")
    suspend fun getAll(): List<BreedEntity>

    @Query("SELECT * FROM BreedEntity WHERE id = :breedId LIMIT 1")
    suspend fun getBreedById(breedId: String): BreedEntity?
}