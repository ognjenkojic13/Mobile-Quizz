package raf.rs.rma.breeds.breedGallery

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import coil.compose.rememberAsyncImagePainter
import raf.rs.rma.breeds.breedsList.CatBreedsViewModel
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

fun NavGraphBuilder.photoViewerScreen(
    route: String,
    navController: NavController
) {
    composable(
        route = route,
        arguments = listOf(
            navArgument("breedId") { type = NavType.StringType },
            navArgument("imageUrl") { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val breedId = backStackEntry.arguments?.getString("breedId")
        val imageUrl = backStackEntry.arguments?.getString("imageUrl")

        breedId?.let { id ->
            imageUrl?.let { url ->
                val decodeUrl = URLDecoder.decode(url, StandardCharsets.UTF_8.toString())
                PhotoViewerScreen(
                    breedId = id,
                    startImageUrl = decodeUrl,
                    navController = navController
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PhotoViewerScreen(
    breedId: String,
    startImageUrl: String,
    navController: NavController
) {
    val viewModel: CatBreedsViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(breedId) {
        viewModel.getBreedImageUrls(breedId)
    }

    val imageUrls = state.imageUrls

    val initialPage = remember(imageUrls, startImageUrl) {
        if (imageUrls.isNotEmpty() && imageUrls.contains(startImageUrl)) {
            imageUrls.indexOf(startImageUrl)
        } else {
            0
        }
    }

    val pagerState = rememberPagerState(initialPage = initialPage, pageCount = {imageUrls.size})

    LaunchedEffect(imageUrls) {
        if (imageUrls.isNotEmpty()) {
            pagerState.scrollToPage(initialPage)
        }
    }

    Scaffold{ paddingValues ->
        if (imageUrls.isNotEmpty()) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.padding(paddingValues)
            ) { page ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                        .clickable(onClick = { navController.popBackStack() })
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(imageUrls[page]),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No images available")
            }
        }
    }
}






