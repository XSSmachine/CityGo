package com.example.citygo.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui_home.HomeScreen
import com.example.ui_home.HomeViewModel
import com.example.ui_users.login.CreateUserDialogScreen
import com.example.ui_users.login.LoginScreen
import com.example.ui_users.login.MainEvent
import com.example.ui_users.login.UserLoginViewModel
import com.example.ui_users.read.PendingScreen
import com.example.ui_users.read.ServiceProviderProfileScreen
import com.example.ui_users.read.UpdateUserProfileDialogScreen
import com.example.ui_users.read.UserProfileScreen
import com.example.ui_users.read.UserProfileViewModel
import com.example.userrequest.create.CreateDetailsUserRequestScreen
import com.example.userrequest.create.CreatePictureUserRequestScreen
import com.example.userrequest.create.CreatePriceUserRequestScreen
import com.example.userrequest.create.CreateUserRequestViewModel
import com.example.userrequest.details.DetailScreen
import com.example.userrequest.details.DetailUserRequestViewModel
import com.example.userrequest.read.ReadAllOffersScreen
import com.example.userrequest.read.ReadUserRequestScreen
import com.example.userrequest.read.ReadUserRequestViewModel
import com.example.userrequest.readAll.ReadAllUserRequestScreen
import com.example.userrequest.readAll.ReadAllUserRequestsViewModel
import com.example.userrequest.update.UpdateUserRequestScreen
import com.example.userrequest.update.UpdateUserRequestViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@Composable
 fun Router(navController: NavHostController,
                   createUserProfileViewModel: UserLoginViewModel,
                   createContactViewModel: CreateUserRequestViewModel,
                   homeViewModel : HomeViewModel,
            detailUserRequestViewModel: DetailUserRequestViewModel,
            updateUserRequestViewModel: UpdateUserRequestViewModel,
                    isLoginRequired:Boolean

) {




//if(isLoginRequired) "login" else "home"
    NavHost(
        navController = navController,
        startDestination = if(isLoginRequired) "login" else "home"
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
                createContactViewModel,
                navigateUp = {navController.navigateUp()},
            )
        }

        composable("details") {

            CreateDetailsUserRequestScreen(
                navController = navController,
                createContactViewModel,
                navigateUp = {navController.navigateUp()},
            )
        }

        composable("price") {

            CreatePriceUserRequestScreen(
                navController = navController,
                createContactViewModel,
                navigateUp = {navController.navigateUp()},
            )
        }

        composable("readMy") {
            val listContactsViewModel: ReadUserRequestViewModel = hiltViewModel()
            ReadUserRequestScreen(
                navController = navController,
                onUserRequestClick = { requestId, userId ->
                    navController.navigateToSingleTop(
                        route = "${"detail"}/$requestId/$userId"
                    )
                },
                onUserRequestButtonClick = { requestId ->
                    navController.navigateToSingleTop(
                        route = "${"allOffers"}/$requestId"
                    )
                },
                listContactsViewModel,
            )
        }


        composable("profile") {
            val userProfileViewModel: UserProfileViewModel = hiltViewModel()
            UserProfileScreen(
                navController = navController,
                userProfileViewModel
            )
        }

        composable("updateDialog") {
            val userProfileViewModel: UserProfileViewModel = hiltViewModel()
            UpdateUserProfileDialogScreen(
                navController = navController,
                userProfileViewModel,
                onDismiss = {navController.popBackStack()}
            )
        }

        composable(
            route="${"detail"}/{requestId}/{userId}",
            arguments = listOf(navArgument("requestId"){
                NavType.LongType
                defaultValue=-1L
            },navArgument("userId"){
                NavType.StringType
                defaultValue=""
            })
        ){backStackEntry ->
            val requestId = backStackEntry.arguments?.getLong("requestId") ?: -1L
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            DetailScreen(
                detailUserRequestViewModel,
                requestId = requestId,
                userId=userId,
                navigateUp = {navController.navigateUp()},
                onUserRequestUpdate = {requestId, userId ->
                    navController.navigateToSingleTop(
                        route = "${"update"}/$requestId/$userId"
                    )
                },
            )

        }
        composable(
            route="${"update"}/{requestId}/{userId}",
            arguments = listOf(navArgument("requestId"){
                NavType.LongType
                defaultValue=-1L
            },navArgument("userId"){
                NavType.StringType
                defaultValue=""
            })
        ){backStackEntry ->
            val id = backStackEntry.arguments?.getLong("requestId") ?: -1L
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            UpdateUserRequestScreen(
                updateUserRequestViewModel,
                requestId = id.toInt(),
                userId = userId,
                navigateUp = {navController.popBackStack()}
            )

        }



        dialog( route = "pending") {
            val userProfileViewModel: UserProfileViewModel = hiltViewModel()
            PendingScreen(
                navController = navController,
                userProfileViewModel
            )
        }

        composable("provider") {
            val userProfileViewModel: UserProfileViewModel = hiltViewModel()
            ServiceProviderProfileScreen(
                navController = navController,
                userProfileViewModel
            )
        }

        composable("readAll") {
            val readAllUserRequestsViewModel:ReadAllUserRequestsViewModel = hiltViewModel()
            ReadAllUserRequestScreen(
                navController = navController,
                onUserRequestClick = {userId,requestId ->
                    navController.navigateToSingleTop(
                        route = "${"detail"}/$userId/$requestId"
                    )
                },
                readAllUserRequestsViewModel
            )
        }

        composable(
            route="${"allOffers"}/{requestId}",
            arguments = listOf(navArgument("requestId"){
                NavType.LongType
                defaultValue=-1L
            })
        ){backStackEntry ->
            val requestId = backStackEntry.arguments?.getLong("requestId") ?: -1L
            val readUserRequestViewModel:ReadUserRequestViewModel = hiltViewModel()
            ReadAllOffersScreen(
                navController,
                requestId = requestId,
                navigateUp = {navController.popBackStack()}


            )

        }




    }
}

fun NavHostController.navigateToSingleTop(route:String){
    navigate(route){
        popUpTo(graph.findStartDestination().id){saveState=true}
        launchSingleTop = true
        restoreState = true
    }
}