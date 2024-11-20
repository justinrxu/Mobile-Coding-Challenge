package com.example.mobilecodingchallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mobilecodingchallenge.ui.theme.MobileCodingChallengeTheme
import com.example.mobilecodingchallenge.ui.views.MediaGalleryView
import com.example.mobilecodingchallenge.ui.views.MainView
import com.example.mobilecodingchallenge.ui.views.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MobileCodingChallengeTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "/mainView") {
                    composable(
                        route = "/mainView",
                        enterTransition = { slideInVertically() + fadeIn() }
                    ) {
                        val mainViewModel: MainViewModel = hiltViewModel()
                        LaunchedEffect(Unit) {
                            mainViewModel.selectedItem = null
                        }
                        MainView(mainViewModel = mainViewModel, navController = navController)
                    }

                    composable(
                        route = "/mediaGalleryView",
                        enterTransition = { slideInVertically() + fadeIn() },
                    ) {
                        val mainViewModel: MainViewModel =
                            navController.previousBackStackEntry?.let { backStackEntry ->
                                hiltViewModel(backStackEntry)
                            } ?: hiltViewModel()
                        MediaGalleryView(
                            mainViewModel = mainViewModel,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}