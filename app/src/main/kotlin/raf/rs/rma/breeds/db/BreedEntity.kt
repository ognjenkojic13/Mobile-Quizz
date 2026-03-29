package raf.rs.rma.breeds.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BreedEntity(
    @PrimaryKey val id: String,
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
    val wikipediaUrl: String?,
    val imageUrl: String?
)