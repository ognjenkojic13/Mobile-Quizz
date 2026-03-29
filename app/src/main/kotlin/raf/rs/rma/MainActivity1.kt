package raf.rs.rma

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import raf.rs.rma.navigation.AppNavigation
import raf.rs.rma.ui.AppTheme

@AndroidEntryPoint
class MainActivity1 : ComponentActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)

                setContent {
                        AppTheme {
                                AppNavigation()
                        }
                }
        }
}

