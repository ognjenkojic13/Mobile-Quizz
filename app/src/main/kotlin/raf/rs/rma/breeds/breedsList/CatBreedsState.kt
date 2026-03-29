package raf.rs.rma.breeds.breedsList

import raf.rs.rma.breeds.db.BreedEntity

data class CatBreedsState(
    val breeds: List<BreedEntity> = emptyList(),
    val loading: Boolean = false,
    val updating: Boolean = false,
    val imageUrls: List<String> = emptyList(),
    val isLoadingImages: Boolean = false
)

