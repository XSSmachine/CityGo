package com.example.citygo.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.citygo.R

sealed class NavigationItem(val route: String, var title: String, val icon: Int) {


    object Home : NavigationItem("home", "Home", R.drawable.baseline_home_filled_24)
    object AllRequestList : NavigationItem("readAll", "Assignements", R.drawable.baseline_emoji_people_24 )
    object MyRequestList : NavigationItem("readMy", "My activities",R.drawable.baseline_checklist_24 )
    object Profile : NavigationItem("profile", "Profile",R.drawable.baseline_account_circle_24 )

}