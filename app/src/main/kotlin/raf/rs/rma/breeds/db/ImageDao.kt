package raf.rs.rma.breeds.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(images: List<ImageEntity>)

    @Query("SELECT * FROM images WHERE breedId = :breedId")
    suspend fun getImagesForBreed(breedId: String): List<ImageEntity>
}
