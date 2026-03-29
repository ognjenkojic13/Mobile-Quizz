package raf.rs.rma.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController, ) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "userRegistration",
            modifier = Modifier.padding(innerPadding)
        ) {
            mainScreens(navController)
        }
    }
}