package raf.rs.rma.breeds.db.mappers

import raf.rs.rma.breeds.api.models.BreedApiModel
import raf.rs.rma.breeds.db.BreedEntity

fun BreedApiModel.toBreedEntity(): BreedEntity {
    return BreedEntity(
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