package raf.rs.rma.breeds.api.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class BreedApiModel(
    val id: String,
    val name: String = "",
    @SerialName("alt_names")
    val altNames: String = "",
    val description: String = "",
    val temperament: String = "",
    val origin: String = "",
    val weight: Weight = Weight("", ""),
    @SerialName("life_span")
    val lifeSpan: String = "",
    val adaptability: Int = 1,
    @SerialName("affection_level")
    val affectionLevel: Int = 1,
    @SerialName("energy_level")
    val energyLevel: Int = 1,
    val intelligence: Int = 1,
    @SerialName("stranger_friendly")
    val strangerFriendly: Int = 1,
    val rare: Int = 1,
    @SerialName("wikipedia_url")
    val wikipediaUrl: String = "",
    val image: Image = Image("", -1, -1, ""),
)

@Serializable
data class Weight(
    val imperial: String,
    val metric: String
)

@Serializable
data class Image(
    val url: String,
    val width: Int,
    val height: Int,
    val id: String
)

fun BreedApiModel.toBreed(): Breed {
    return Breed(
        id = this.id,
        name = this.name,
        altNames = if (this.altNames.isNotEmpty()) this.altNames.split(", ") else emptyList(),
        description = this.description,
        temperament = if (this.temperament.isNotEmpty()) this.temperament.split(", ") else emptyList(),
        origin = this.origin,
        weight = this.weight.imperial,
        lifeSpan = this.lifeSpan,
        adaptability = this.adaptability,
        affectionLevel = this.affectionLevel,
        energyLevel = this.energyLevel,
        intelligence = this.intelligence,
        strangerFriendly = this.strangerFriendly,
        rare = this.rare == 1,
        wikipediaUrl = this.wikipediaUrl,
        imageUrl = this.image.url
    )
}

data class Breed(
    val id: String,
    val name: String,
    val altNames: List<String>,
    val description: String,
    val temperament: List<String>,
    val origin: String,
    val weight: String,
    val lifeSpan: String,
    val adaptability: Int,
    val affectionLevel: Int,
    val energyLevel: Int,
    val intelligence: Int,
    val strangerFriendly: Int,
    val rare: Boolean,
    val wikipediaUrl: String,
    val imageUrl: String
)
