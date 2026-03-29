package raf.rs.rma.account

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import raf.rs.rma.users.UserState
import raf.rs.rma.users.UserViewModel
import java.util.Date

@ExperimentalMaterial3Api
fun NavGraphBuilder.accountDetailsScreen(
    route: String,
    navController: NavController
) = composable(route = route) {
    val userViewModel: UserViewModel = hiltViewModel()
    val state by userViewModel.state.collectAsState()
    AccountDetailsScreen(state = state, navController = navController)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDetailsScreen(
    state: UserState,
    navController: NavController
) {
    state.currentUser?.let { currentUser ->

        Scaffold (
            topBar = {
                TopAppBar(
                    title = { Text("Account details") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(text = "Ime: ${currentUser.name}", style = MaterialTheme.typography.headlineLarge)
                    Text(text = "Nickname: ${currentUser.nickname}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Email: ${currentUser.email}", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { navController.navigate("editAccount") }) {
                        Text("Izmeni podatke")
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Rezultati kvizova:")
                    state.quizResults.forEach { result ->
                        Text("Rezultat: ${result.score} - Datum: ${Date(result.timestamp)}")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    state.bestQuizResult?.let { result ->
                        Text("Najbolji rezultat: ${result.score}")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    state.bestLeaderboardPosition?.let { position ->
                        Text("Najbolja pozicija na leaderboardu: $position")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Broj odigranih kvizova: ${state.quizCount}")
                }
            }
        )

    } ?: run {
        Text("Nema dostupnih podataka o nalogu.")
    }
}
