package raf.rs.rma.account

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import raf.rs.rma.users.UserState
import raf.rs.rma.users.UserViewModel

@ExperimentalMaterial3Api
fun NavGraphBuilder.editAccountScreen(
    route: String,
    navController: NavController
) = composable(route = route) {
    val userViewModel: UserViewModel = hiltViewModel()
    val state by userViewModel.state.collectAsState()
    EditAccountScreen(
        state = state,
        onUpdateUser = { name, nickname, email, onValidationFailed, onSuccess ->
            userViewModel.updateUser(
                name, nickname, email,
                onValidationFailed,
                onSuccess
            )
        },
        navController = navController
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EditAccountScreen(
    state: UserState,
    onUpdateUser: (
        name: String,
        username: String,
        email: String,
        onValidationFailed: (String) -> Unit,
        onSuccess: () -> Unit
    ) -> Unit,
    navController: NavController
) {
    state.currentUser?.let { currentUser ->
        var name by remember { mutableStateOf(TextFieldValue(currentUser.name)) }
        var nickname by remember { mutableStateOf(TextFieldValue(currentUser.nickname)) }
        var email by remember { mutableStateOf(TextFieldValue(currentUser.email)) }
        var errorMessage by remember { mutableStateOf("") }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Edit Account") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            },
            content = { paddingValues ->
                Column(modifier = Modifier.padding(16.dp).padding(paddingValues)) {
                    TextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Ime") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    TextField(
                        value = nickname,
                        onValueChange = { nickname = it },
                        label = { Text("Username") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (errorMessage.isNotEmpty()) {
                        Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            onUpdateUser(
                                name.text,
                                nickname.text,
                                email.text,
                                { errorMessage = it },
                                {
                                    navController.navigate("accountDetails") {
                                        popUpTo("editAccount") { this.inclusive = true }
                                    }
                                }
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Sačuvaj izmene")
                    }
                }
            }
        )


    } ?: run {
        Text("Nema dostupnih podataka o nalogu.")
    }
}
