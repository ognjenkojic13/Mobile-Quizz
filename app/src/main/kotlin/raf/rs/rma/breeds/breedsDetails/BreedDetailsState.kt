package raf.rs.rma.breeds.breedsDetails

import raf.rs.rma.breeds.db.BreedEntity

data class BreedDetailsState(
    val breed: BreedEntity? = null,
    val isLoading: Boolean = false
)