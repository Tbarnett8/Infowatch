package com.example.infowatch.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.infowatch.navigation.Screen
import com.example.infowatch.ui.theme.BottomBar
import com.example.infowatch.ui.theme.White

@Composable
fun BottomBar(navController: NavHostController) {

    val items = listOf(
        Screen.Heroes,
        Screen.Maps,
        Screen.Profile
    )
    val currentRoute =
        navController.currentBackStackEntryAsState().value?.destination?.route

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(BottomBar)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .align(Alignment.TopCenter)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.35f),
                            Color.Transparent
                        )
                    )
                )
        )

        NavigationBar(
            containerColor = Color.White.copy(alpha = 0.05f)
        ) {
            items.forEachIndexed { index, screen ->
                val selected = currentRoute == screen.route

                NavigationBarItem(
                    selected = selected,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(Screen.Heroes.route)
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = screen.icon,
                            contentDescription = screen.label,
                        )
                    },
                    label = {
                        Text(
                            text = screen.label,
                        )
                    },
                    alwaysShowLabel = true,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = White,
                        selectedTextColor = White,

                        unselectedIconColor = Color.White.copy(alpha = 0.4f),
                        unselectedTextColor = Color.White.copy(alpha = 0.4f),

                        indicatorColor = Color.Transparent
                    )
                )

                if (index < items.lastIndex) {
                    Box(
                        modifier = Modifier
                            .height(56.dp)
                            .width(1.dp)
                            .align(Alignment.CenterVertically)
                            .background(White.copy(alpha = 0.2f))
                    )
                }
            }
        }
    }
}