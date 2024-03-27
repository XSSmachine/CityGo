package com.example.citygo.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ui_home.HomeScreen
import com.example.ui_home.HomeViewModel
import com.example.ui_users.login.CreateUserDialogScreen
import com.example.ui_users.login.LoginScreen
import com.example.ui_users.login.UserLoginViewModel
import com.example.userrequest.create.CreateDetailsUserRequestScreen
import com.example.userrequest.create.CreatePictureUserRequestScreen
import com.example.userrequest.create.CreatePriceUserRequestScreen
import com.example.userrequest.create.CreateUserRequestViewModel
import com.example.userrequest.read.ReadUserRequestScreen
import com.example.userrequest.read.ReadUserRequestViewModel
import kotlinx.coroutines.runBlocking

@Composable
 fun Router(navController: NavHostController,
                   createUserProfileViewModel: UserLoginViewModel,
                   createContactViewModel: CreateUserRequestViewModel,
                   homeViewModel : HomeViewModel
) {
    val userUid = runBlocking {createUserProfileViewModel.readUserId()  }
    val isLoggedIn = userUid.toString().isNotEmpty()
    val initialRoute = if(isLoggedIn) "home" else "login"

    NavHost(
        navController = navController,
        startDestination = initialRoute
    ) {

        composable("login") {

            LoginScreen(
                navController = navController,
                createUserProfileViewModel
            )
        }
        composable("dialog") {

            CreateUserDialogScreen(
                navController = navController,
                createUserProfileViewModel
            )
        }

        composable("home") {

            HomeScreen(
                navController = navController,
                homeViewModel
            )
        }

        composable("picture") {

            CreatePictureUserRequestScreen(
                navController = navController,
                createContactViewModel
            )
        }

        composable("details") {

            CreateDetailsUserRequestScreen(
                navController = navController,
                createContactViewModel
            )
        }

        composable("price") {

            CreatePriceUserRequestScreen(
                navController = navController,
                createContactViewModel
            )
        }

        composable("read") {
            val listContactsViewModel: ReadUserRequestViewModel = hiltViewModel()
            ReadUserRequestScreen(
                navController = navController,
                listContactsViewModel
            )
        }


    }
}