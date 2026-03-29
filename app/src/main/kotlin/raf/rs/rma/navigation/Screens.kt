package raf.rs.rma.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import raf.rs.rma.account.accountDetailsScreen
import raf.rs.rma.account.editAccountScreen
import raf.rs.rma.breeds.breedGallery.catBreedGalleryScreen
import raf.rs.rma.breeds.breedGallery.photoViewerScreen
import raf.rs.rma.breeds.breedsDetails.catBreedDetailsScreen
import raf.rs.rma.breeds.breedsList.catBreedsScreen
import raf.rs.rma.quiz.leaderboard.leaderboardScreen
import raf.rs.rma.quiz.quizResultScreen
import raf.rs.rma.quiz.quizScreen
import raf.rs.rma.quiz.startQuizScreen
import raf.rs.rma.users.userRegistrationScreen

@ExperimentalMaterial3Api
fun NavGraphBuilder.mainScreens(navController: NavController) {
    catBreedsScreen(
        route = Screen.Home.route,
        navController = navController,
    )
    startQuizScreen(
        route = Screen.Quiz.route,
        navController = navController
    )
    leaderboardScreen(
        route = Screen.Leaderboard.route,
        navController = navController
    )
    accountDetailsScreen(
        route = Screen.Account.route,
        navController = navController
    )
    userRegistrationScreen(
        route = "userRegistration",
        navController = navController
    )
    catBreedDetailsScreen(
        route = "catBreedDetails/{id}",
        navController = navController
    )
    editAccountScreen(
        route = "editAccount",
        navController = navController
    )
    catBreedGalleryScreen(
        route = "catBreedGallery/{breedId}",
        navController = navController
    )
    photoViewerScreen(
        route = "photoViewer/{breedId}/{imageUrl}",
        navController = navController
    )
    quizScreen(
        route = "quizScreen",
        navController = navController
    )
    quizResultScreen(
        route = "quizResultScreen",
        navController = navController
    )
}
