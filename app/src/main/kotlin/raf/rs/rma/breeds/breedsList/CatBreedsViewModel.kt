package raf.rs.rma.breeds.breedsList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import raf.rs.rma.breeds.repository.CatBreedRepository
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class CatBreedsViewModel @Inject constructor(
    private val repository: CatBreedRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CatBreedsState())
    val state = _state.asStateFlow()
    private fun setState(reducer: CatBreedsState.() -> CatBreedsState) = _state.update(reducer)

    init {
        fetchAllBreeds()
        observeBreeds()
    }

    private fun observeBreeds() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            repository.observeAllBreeds()
                .distinctUntilChanged()
                .collect {
                    setState {
                        copy(
                            loading = false,
                            breeds = it
                        )
                    }
                }
        }
    }

    private fun fetchAllBreeds() {
        viewModelScope.launch {
            setState { copy(updating = true) }
            try {
                withContext(Dispatchers.IO) {
                    repository.fetchAllBreeds()
                }
            } catch (e: UnknownHostException) {
                Log.e("NetworkError", "Failed to connect to the API", e)
            } catch (e: Exception) {
                Log.e("DatabaseError", "Failed to insert breeds into database", e)
            } finally {
                setState { copy(updating = false) }
            }
        }
    }

    fun getBreedImageUrls(breedId: String) {
        viewModelScope.launch {
            try {
                val imageUrls = repository.getBreedImageUrls(breedId)
                _state.update { it.copy(imageUrls = imageUrls, isLoadingImages = false) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoadingImages = false) }
            }
        }
    }
}
