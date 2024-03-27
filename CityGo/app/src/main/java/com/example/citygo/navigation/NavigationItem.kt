package com.example.citygo.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.citygo.R

sealed class NavigationItem(val route: String, var title: String, val icon: ImageVector) {


    object Home : NavigationItem("home", "Home", Icons.Default.Home )
    object RequestList : NavigationItem("read", "Your Requests", Icons.Default.Menu )
    object Profile : NavigationItem("profile", "Profile",Icons.Default.AccountCircle )

}