package raf.rs.rma.quiz

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import raf.rs.rma.navigation.Screen
import raf.rs.rma.quiz.leaderboard.LeaderboardViewModel

@ExperimentalMaterial3Api
fun NavGraphBuilder.quizResultScreen(
    route: String,
    navController: NavController
) = composable(
    route = "$route/{score}",
    arguments = listOf(navArgument("score") { type = NavType.StringType })
) { backStackEntry ->
    val scoreString = backStackEntry.arguments?.getString("score") ?: "0f"
    val score = scoreString.toFloat()
    val viewModel: LeaderboardViewModel = hiltViewModel()

    QuizResultScreen(
        score = score,
        onShare = {
            viewModel.shareResult(score) {
                navController.navigate(Screen.Leaderboard.route) {
                    popUpTo(Screen.Leaderboard.route) { inclusive = true }
                }
            }
        },
        onBackToHome = {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Home.route) { inclusive = true }
            }
        }
    )
}

@Composable
fun QuizResultScreen(
    score: Float,
    onShare: () -> Unit,
    onBackToHome: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Your Score: $score")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onShare, modifier = Modifier.fillMaxWidth()) {
            Text("Share on Leaderboard")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBackToHome, modifier = Modifier.fillMaxWidth()) {
            Text("Back to Home")
        }
    }
}
