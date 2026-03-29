package raf.rs.rma.breeds.breedsList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import raf.rs.rma.breeds.db.BreedEntity

@ExperimentalMaterial3Api
fun NavGraphBuilder.catBreedsScreen(
    route: String,
    navController: NavController,
) = composable(route = route) {
    val catBreedsScreenViewModel: CatBreedsViewModel = hiltViewModel()
    val state by catBreedsScreenViewModel.state.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var isSearchVisible by remember { mutableStateOf(false) }

    if (searchQuery.isEmpty()) {
        state.breeds
    } else {
        state.breeds.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }

    CatBreedsScreen(
        state = state,
        searchQuery = searchQuery,
        isSearchVisible = isSearchVisible,
        onSearchQueryChange = { searchQuery = it },
        onSearchVisibilityChange = { isSearchVisible = it },
        onBreedClick = { breedId -> navController.navigate("catBreedDetails/$breedId") }
    )
}

@ExperimentalMaterial3Api
@Composable
fun CatBreedsScreen(
    state: CatBreedsState,
    searchQuery: String,
    isSearchVisible: Boolean,
    onSearchQueryChange: (String) -> Unit,
    onSearchVisibilityChange: (Boolean) -> Unit,
    onBreedClick: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (!isSearchVisible) {
                        Text(text = "Cat Breeds")
                    }
                },
                actions = {
                    if (isSearchVisible) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = onSearchQueryChange,
                            leadingIcon = {
                                IconButton(onClick = {
                                    onSearchVisibilityChange(false)
                                    onSearchQueryChange("")
                                }) {
                                    Icon(Icons.Filled.Close, contentDescription = "Close search")
                                }
                            },
                            placeholder = { Text("Search breeds") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(onSearch = {
                                defaultKeyboardAction(ImeAction.Search)
                            })
                        )
                    } else {
                        IconButton(onClick = { onSearchVisibilityChange(true) }) {
                            Icon(Icons.Filled.Search, contentDescription = "Search")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        if (state.loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(contentPadding = paddingValues) {
                items(state.breeds.filter { breed ->
                    breed.name.contains(searchQuery, ignoreCase = true)
                }) { breed ->
                    CatBreedItem(breed = breed) {
                        onBreedClick(breed.id)
                    }
                }
            }
        }
    }
}


@Composable
fun CatBreedItem(breed: BreedEntity, onBreedClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onBreedClick() },
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = breed.name)
            Text(
                text = breed.description.take(250),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Row(modifier = Modifier.padding(top = 8.dp)) {
                breed.temperament.take(3).forEach { temperament ->
                    Chip(text = temperament)
                }
            }
        }
    }
}

@Composable
fun Chip(text: String) {
    Surface(
        modifier = Modifier.padding(4.dp),
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = text)
        }
    }
}
