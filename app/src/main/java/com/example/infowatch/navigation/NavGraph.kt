package com.example.infowatch.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.infowatch.ui.heroes.HeroDetailScreen
import com.example.infowatch.ui.heroes.HeroesScreen
import com.example.infowatch.ui.maps.MapDetailScreen
import com.example.infowatch.ui.maps.MapsScreen
import com.example.infowatch.ui.profile.ProfileScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
) {

    NavHost(
        navController = navController,
        startDestination = Screen.Heroes.route
    ) {
        composable(Screen.Heroes.route) {
            HeroesScreen(
                modifier = Modifier.padding(paddingValues),
                onHeroClick = { heroKey ->
                    navController.navigate("heroDetail/$heroKey")
                }
            )
        }

        composable(
            route = Screen.HeroDetail.route,
            arguments = listOf(navArgument("heroKey") { type = NavType.StringType })
        ) { backStackEntry ->
            val heroKey = backStackEntry.arguments?.getString("heroKey") ?: ""
            HeroDetailScreen(
                heroKey = heroKey,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Maps.route) {
            MapsScreen(
                modifier = Modifier.padding(paddingValues),
                onMapClick = { mapKey ->
                    navController.navigate("mapDetail/$mapKey")
                }
            )
        }

        composable(
            route = Screen.MapDetail.route,
            arguments = listOf(navArgument("mapKey") { type = NavType.StringType })
        ) { backStackEntry ->
            val mapKey = backStackEntry.arguments?.getString("mapKey") ?: ""
            MapDetailScreen(
                mapKey = mapKey,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen()
        }
    }
}