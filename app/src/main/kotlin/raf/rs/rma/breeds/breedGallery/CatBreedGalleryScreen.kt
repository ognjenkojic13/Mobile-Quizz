package raf.rs.rma.breeds.breedGallery

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import coil.compose.rememberAsyncImagePainter
import raf.rs.rma.breeds.breedsList.CatBreedsViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@ExperimentalMaterial3Api
fun NavGraphBuilder.catBreedGalleryScreen(
    route: String,
    navController: NavController
) = composable(
    route = route,
    arguments = listOf(navArgument(name = "breedId") { this.type = NavType.StringType })
) { backStackEntry ->
    val breedId = backStackEntry.arguments?.getString("breedId")
        ?: throw IllegalArgumentException("Breed ID is required.")

    CatBreedGalleryScreen(
        breedId = breedId,
        navController = navController
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatBreedGalleryScreen(
    breedId: String,
    navController: NavController
) {
    val viewModel: CatBreedsViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(breedId) {
        viewModel.getBreedImageUrls(breedId)
    }

    val breed = state.breeds.find { it.id == breedId }

    breed?.let {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Gallery of ${breed.name}") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) { paddingValues ->
            if (state.isLoadingImages) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(8.dp),
                    modifier = Modifier.padding(paddingValues)
                ) {
                    items(state.imageUrls) { imageUrl ->
                        val encodedImageUrl = URLEncoder.encode(imageUrl, StandardCharsets.UTF_8.toString())
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Gray)
                                .clickable {
                                    navController.navigate("photoViewer/$breedId/$encodedImageUrl")
                                }
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(imageUrl),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    } ?: run {
        Text("Breed not found.")
    }
}
