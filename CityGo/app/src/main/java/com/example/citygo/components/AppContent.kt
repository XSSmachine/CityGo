package com.example.citygo.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization.Companion.Characters
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.citygo.navigation.BottomNavigationBar
import com.example.citygo.navigation.NavigationItem
import com.example.citygo.navigation.Router
import com.example.ui_home.HomeViewModel
import com.example.ui_users.login.UserLoginViewModel
import com.example.userrequest.create.CreateUserRequestViewModel


@ExperimentalAnimationApi
@Composable
fun AppContent() {
    val navController = rememberNavController()
    val createUserProfileViewModel: UserLoginViewModel = hiltViewModel()
    val createContactViewModel: CreateUserRequestViewModel = hiltViewModel()
    val homeViewModel:HomeViewModel = hiltViewModel()

    val bottomScreens = listOf(
        NavigationItem.Home.route,
        NavigationItem.RequestList.route,
        NavigationItem.Profile.route,
    )

    val showNavBar = navController
        .currentBackStackEntryAsState().value?.destination?.route in bottomScreens

    Scaffold(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        bottomBar = {
            if (showNavBar) {
                BottomNavigationBar(navController)
            }
        }
    ) {
        // Apply padding to the content inside the lambda parameter
        Box(
            modifier = Modifier.padding(it)
        ) {
            Router(navController = navController, createUserProfileViewModel, createContactViewModel,homeViewModel)
        }
    }

}