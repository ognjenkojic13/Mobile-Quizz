package raf.rs.rma.breeds.breedsDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import raf.rs.rma.breeds.repository.CatBreedRepository
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class BreedDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: CatBreedRepository
) : ViewModel() {
    private val _detailsState = MutableStateFlow(BreedDetailsState(isLoading = true))
    val detailsState: StateFlow<BreedDetailsState> = _detailsState.asStateFlow()

    init {
        val breedId: String? = savedStateHandle["id"]
        breedId?.let { loadBreedDetails(it) }
    }

    private fun loadBreedDetails(breedId: String) {
        viewModelScope.launch {
            _detailsState.value = BreedDetailsState(isLoading = true)
            try {
                val breed = withContext(Dispatchers.IO) {
                    repository.getBreedById(breedId)
                }
                _detailsState.value = BreedDetailsState(breed = breed, isLoading = false)
            } catch (error: IOException) {
                _detailsState.value = BreedDetailsState(isLoading = false)
            }
        }
    }
}
