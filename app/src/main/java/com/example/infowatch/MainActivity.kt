package com.example.infowatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.infowatch.components.BottomBar
import com.example.infowatch.navigation.NavGraph
import com.example.infowatch.navigation.Screen
import com.example.infowatch.ui.theme.Blue
import com.example.infowatch.ui.theme.DarkBlue

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            val showBottomBar = when (currentRoute) {
                Screen.HeroDetail.route,
                Screen.MapDetail.route -> false
                else -> true
            }

            Scaffold(
                bottomBar = {
                    if (showBottomBar) {
                        BottomBar(navController)
                    }
                }
            ) { padding ->

                Box(
                    modifier = Modifier
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    DarkBlue,
                                    Blue
                                ),
                                start = Offset(0f, 0f),
                                end = Offset(1000f, 1000f)
                            )
                        )
                ) {
                    NavGraph(
                        navController = navController,
                        paddingValues = padding
                    )
                }
            }
        }
    }
}