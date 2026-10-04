package com.example.infowatch.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Heroes : Screen("heroes", "Heroes", Icons.Default.People)
    object HeroDetail: Screen("heroDetail/{heroKey}", "Hero Detail", Icons.Default.Person)
    object Maps : Screen("maps", "Maps", Icons.Default.Map)
    object MapDetail: Screen("mapDetail/{mapKey}", "Map Detail", Icons.Default.Map)
    object Profile : Screen("profile", "Profile", Icons.Default.AccountBox)
}