package com.raphaelfavero.stylearena

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.raphaelfavero.stylearena.ranking.RankingScreen
import com.raphaelfavero.stylearena.stylearena.StyleArenaScreen
import com.raphaelfavero.stylearena.styleupload.StyleUploadScreen
import com.raphaelfavero.stylearena.ui.theme.StyleArenaTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StyleArenaTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = StyleUploadRoute
                ) {
                    composable<StyleUploadRoute> {
                        StyleUploadScreen {
                            navController.navigate(StyleArenaRoute) {
                                popUpTo(StyleUploadRoute) { inclusive = true }
                            }
                        }
                    }
                    composable<StyleArenaRoute> {
                        StyleArenaScreen {
                            navController.navigate(DailyRankingRoute) {
                                popUpTo(StyleArenaRoute) { inclusive = true }
                            }
                        }
                    }
                    composable<DailyRankingRoute> {
                        RankingScreen()
                    }
                }
            }
        }
    }
}

@Serializable
object StyleUploadRoute

@Serializable
object StyleArenaRoute

@Serializable
object DailyRankingRoute



