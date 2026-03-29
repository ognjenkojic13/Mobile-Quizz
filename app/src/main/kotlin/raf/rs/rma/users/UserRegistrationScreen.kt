package raf.rs.rma.users

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
fun NavGraphBuilder.userRegistrationScreen(
    route: String,
    navController: NavController
) = composable(route = route) {
    val userViewModel: UserViewModel = hiltViewModel()
    val state by userViewModel.state.collectAsState()

    if (state.users.isNotEmpty()) {
        navController.navigate("main") {
            popUpTo(route) { inclusive = true }
        }
    } else {
        UserRegistrationScreen(
            onUserCreated = { newUser ->
                userViewModel.createUser(
                    name = newUser.name,
                    nickname = newUser.nickname,
                    email = newUser.email,
                    onValidationFailed = { error ->
                    },
                    onSuccess = {
                        navController.navigate("main") {
                            popUpTo(route) { inclusive = true }
                        }
                    }
                )
            }
        )
    }
}

@Composable
fun UserRegistrationScreen(
    viewModel: UserViewModel = hiltViewModel(),
    onUserCreated: (UserUiModel) -> Unit
) {
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var nickname by remember { mutableStateOf(TextFieldValue("")) }
    var errorMessage by remember { mutableStateOf("") }
    var email by remember { mutableStateOf(TextFieldValue("")) }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.padding(16.dp)) {
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
        Button(
            onClick = {
                if (!viewModel.validateNickname(nickname.text)) {
                    errorMessage = "Nickname može sadržati samo slova, brojeve i underscore."
                    return@Button
                }

                if (!viewModel.validateEmailFormat(email.text)) {
                    errorMessage = "Email adresa nije u validnom formatu."
                    return@Button
                }

                coroutineScope.launch {
                    val newUser = UserUiModel(
                        id = 0,
                        name = name.text,
                        nickname = nickname.text,
                        email = email.text
                    )
                    onUserCreated(newUser)
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Create Account")
        }
    }
}
