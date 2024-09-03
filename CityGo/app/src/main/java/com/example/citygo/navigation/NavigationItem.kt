package com.example.citygo.navigation

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.example.citygo.R

sealed class NavigationItem(val route: String,@StringRes val titleResId: Int, val icon: Int) {


    object Home : NavigationItem("home",  R.string.navbar_home, R.drawable.baseline_home_filled_24)
    object AllRequestList : NavigationItem("readAll", R.string.navbar_assign, R.drawable.baseline_emoji_people_24 )
    object MyRequestList : NavigationItem("readMy", R.string.navbar_activity,R.drawable.baseline_checklist_24 )
    object Profile : NavigationItem("profile", R.string.navbar_profile,R.drawable.baseline_account_circle_24 )

    fun getTitle(context: Context): String {
        return context.getString(titleResId)
    }
}