package com.mansur.walawili

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mansur.walawili.ui.screens.destination.DestinationScreen
import com.mansur.walawili.ui.screens.trip.Screen
import com.mansur.walawili.ui.screens.trip.TripPlanningScreen
import com.mansur.walawili.ui.screens.trip.TripPlanningViewModel
import com.mansur.walawili.ui.theme.WalaWiliTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WalaWiliTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF2F1EE)
                ) {
                    val viewModel: TripPlanningViewModel = viewModel()
                    val currentScreen by viewModel.currentScreen

                    AnimatedContent(
                        targetState = currentScreen,
                        transitionSpec = {
                            when (targetState) {
                                is Screen.Destination -> slideInVertically(
                                    initialOffsetY = { it },
                                    animationSpec = tween(350)
                                ) togetherWith slideOutVertically(
                                    targetOffsetY = { -it / 8 },
                                    animationSpec = tween(350)
                                )
                                else -> slideInVertically(
                                    initialOffsetY = { it / 8 },
                                    animationSpec = tween(300)
                                ) togetherWith slideOutVertically(
                                    targetOffsetY = { it },
                                    animationSpec = tween(300)
                                )
                            }
                        },
                        label = "screen_transition"
                    ) { screen ->
                        when (screen) {
                            is Screen.TripPlanning -> TripPlanningScreen(
                                viewModel = viewModel,
                                onBackClick = {}
                            )
                            is Screen.Destination -> DestinationScreen(
                                currentDestination = viewModel.tripDetails.value.destination,
                                onDestinationSelected = { viewModel.updateDestination(it) },
                                onBackClick = { viewModel.navigateBack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
