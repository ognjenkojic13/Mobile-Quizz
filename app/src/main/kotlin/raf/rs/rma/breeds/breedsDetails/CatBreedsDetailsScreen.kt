package raf.rs.rma.breeds.breedsDetails

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import coil.compose.rememberAsyncImagePainter
import raf.rs.rma.breeds.db.BreedEntity

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalMaterial3Api
fun NavGraphBuilder.catBreedDetailsScreen(
    route: String,
    navController: NavController
) = composable(
    route = route,
    arguments = listOf(navArgument(name = "id") { this.type = NavType.StringType })
) { backStackEntry ->
    val breedDetailsViewModel: BreedDetailsViewModel = hiltViewModel()

    val state by breedDetailsViewModel.detailsState.collectAsState()

    CatBreedDetailsScreen(
        state = state,
        navController = navController
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CatBreedDetailsScreen(
    state: BreedDetailsState,
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = state.breed?.name ?: "Loading Breed...", style = MaterialTheme.typography.headlineMedium) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Go back")
                    }
                }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            contentAlignment = Alignment.Center) {
            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                state.breed?.let { breed ->
                    BreedDetailsContent(breed, LocalContext.current, navController)
                } ?: Text("Breed details not available.")
            }
        }
    }
}

@Composable
fun BreedDetailsContent(breed: BreedEntity, context: Context, navController: NavController) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        if (!breed.imageUrl.isNullOrEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(breed.imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(shape = RoundedCornerShape(8.dp))
            )
            Log.d("slika", breed.imageUrl)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Name: ${breed.name}", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        if (breed.altNames.isNotEmpty()) {
            Text("Alt Name: ${breed.altNames}", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))
        }
        Text("Description: ${breed.description}", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Origin: ${breed.origin}", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Temperament: ${breed.temperament}", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Life Span: ${breed.lifeSpan}", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Weight: ${breed.weight}", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(10.dp))

        Divider(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
            thickness = 2.dp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        breed.adaptability?.let { RatingBar(it, "Adaptability") }
        breed.affectionLevel?.let { RatingBar(it, "Affection Level") }
        breed.energyLevel?.let { RatingBar(it, "Energy Level") }
        breed.strangerFriendly?.let { RatingBar(it, "Stranger Friendly") }
        breed.intelligence?.let { RatingBar(it, "Intelligence") }

        Spacer(modifier = Modifier.height(8.dp))

        if (breed.wikipediaUrl != null) {
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = { openUrl(context, breed.wikipediaUrl!!) }
            ) {
                Text("Learn more on Wikipedia")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { navController.navigate("catBreedGallery/${breed.id}") },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("View Gallery")
        }
    }
}

@Composable
fun RatingBar(rating: Int, label: String) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text("$label:", modifier = Modifier.width(100.dp))
        Row(modifier = Modifier.padding(start = 8.dp)) {
            for (i in 1..5) {
                Icon(
                    imageVector = if (i <= rating) Icons.Filled.Star else Icons.Outlined.Star,
                    contentDescription = "$label rating $i",
                    tint = if (i <= rating) Color.Yellow else Color.Gray
                )
            }
        }
    }
}

fun openUrl(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(url)
    }
    context.startActivity(intent)
}

