package raf.rs.rma.breeds.api

import kotlinx.serialization.Serializable
import raf.rs.rma.breeds.api.models.BreedApiModel
import retrofit2.http.GET
import retrofit2.http.Query

interface CatApi {

    @GET("breeds")
    suspend fun listBreeds(): List<BreedApiModel>

    @GET("images/search")
    suspend fun listBreedImages(
        @Query("breed_ids") breedId: String,
        @Query("limit") limit: Int = 10,
        @Query("format") format: String = "json"
    ): List<ImageApiModel>
}

@Serializable
data class ImageApiModel(
    val id: String,
    val url: String
)