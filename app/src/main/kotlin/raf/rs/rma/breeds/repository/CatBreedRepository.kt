package raf.rs.rma.breeds.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import raf.rs.rma.breeds.api.CatApi
import raf.rs.rma.breeds.db.BreedEntity
import raf.rs.rma.breeds.db.ImageEntity
import raf.rs.rma.breeds.db.mappers.toBreedEntity
import raf.rs.rma.db.AppDatabase
import java.io.IOException
import java.net.UnknownHostException
import javax.inject.Inject

class CatBreedRepository @Inject constructor(
    private val catApi: CatApi,
    private val database: AppDatabase,
) {

    suspend fun fetchAllBreeds() {
        try {
            val breeds = catApi.listBreeds()
            Log.d("CatApi", "Fetched breeds from API: $breeds")
            database.breedDao().insertAll(breeds.map { it.toBreedEntity() })
            Log.d("Database", "Inserted breeds into database")
        } catch (e: UnknownHostException) {
            Log.e("NetworkError", "Failed to connect to the API", e)
        } catch (e: IOException) {
            Log.e("NetworkError", "Network error", e)
        }
    }

    fun observeAllBreeds(): Flow<List<BreedEntity>> {
        return database.breedDao().observeAllBreeds()
    }

    suspend fun getAllBreeds(): List<BreedEntity> {
        return database.breedDao().getAll()
    }

    suspend fun getBreedById(breedId: String): BreedEntity? {
        return database.breedDao().getBreedById(breedId)
    }

    suspend fun getBreedImageUrls(breedId: String): List<String> {
        return withContext(Dispatchers.IO) {
            val localImages = database.imageDao().getImagesForBreed(breedId)
            if (localImages.isNotEmpty()) {
                localImages.map { it.url }
            } else {
                try {
                    val imageApiModels = catApi.listBreedImages(breedId)
                    val imageEntities = imageApiModels.map { ImageEntity(breedId = breedId, url = it.url) }
                    database.imageDao().insertAll(imageEntities)
                    imageApiModels.map { it.url }
                } catch (e: Exception) {
                    emptyList()
                }
            }
        }
    }
}