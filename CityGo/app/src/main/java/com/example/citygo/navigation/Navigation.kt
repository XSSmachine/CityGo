package com.example.citygo.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import com.example.ui_home.HomeScreen
import com.example.ui_home.HomeViewModel
import com.example.ui_users.login.CreateUserDialogScreen
import com.example.ui_users.login.LoginScreen
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
import com.example.userrequest.details.AllDetailScreen
import com.example.userrequest.details.DetailScreen
import com.example.userrequest.details.DetailUserRequestViewModel
import com.example.userrequest.read.ReadAllOffersScreen
import com.example.userrequest.read.ReadUserRequestScreen
import com.example.userrequest.read.ReadUserRequestViewModel
import com.example.userrequest.readAll.ReadAllUserRequestScreen
import com.example.userrequest.readAll.ReadAllUserRequestsViewModel
import com.example.userrequest.update.UpdateUserRequestScreen
import com.example.userrequest.update.UpdateUserRequestViewModel

@Composable
 fun Router(navController: NavHostController,
                   createUserProfileViewModel: UserLoginViewModel,
                   createContactViewModel: CreateUserRequestViewModel,
                   homeViewModel : HomeViewModel,
            detailUserRequestViewModel: DetailUserRequestViewModel,
            updateUserRequestViewModel: UpdateUserRequestViewModel,
                    isLoginRequired:Boolean

) {

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
                navigateUp = {navController.popBackStack()},
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
                    navController.navigate(
                        route = "${"detail"}/$requestId/$userId"
                    )
                },
                onUserRequestButtonClick = { requestId ->
                    navController.navigate(
                        route = "${"allOffers"}/$requestId"
                    )
                },
                onCygoOfferClick= {userId,sid ->
                    navController.navigate(
                        route = "${"alldetail"}/$userId/$sid "
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
                NavType.StringType
                defaultValue=""
            },navArgument("userId"){
                NavType.StringType
                defaultValue=""
            }

            )
        ){backStackEntry ->
            val requestId = backStackEntry.arguments?.getString("requestId") ?: ""
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            DetailScreen(
                detailUserRequestViewModel,
                requestId = requestId,
                userId =userId,
                navigateUp = {navController.navigateUp()},
                onUserRequestUpdate = { requestId, userId ->
                    navController.navigate(
                        route = "${"update"}/$requestId/$userId"
                    )
                },
            )

        }


        composable(
            route="${"alldetail"}/{userId}/{sid}",
            arguments = listOf(navArgument("userId"){
                NavType.StringType
                defaultValue=""
            },navArgument("sid"){
                NavType.StringType
                defaultValue=""
            }

            )
        ){backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            val sid = backStackEntry.arguments?.getString("sid") ?: ""
            AllDetailScreen(
                detailUserRequestViewModel,
                userId =userId,
                sid = sid,
                navigateUp = {navController.navigateUp()},

            )

        }

        composable(
            route="${"update"}/{requestId}/{userId}",
            arguments = listOf(navArgument("requestId"){
                NavType.StringType
                defaultValue=""
            },navArgument("userId"){
                NavType.StringType
                defaultValue=""
            })
        ){backStackEntry ->
            val id = backStackEntry.arguments?.getString("requestId") ?: ""
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            UpdateUserRequestScreen(
                updateUserRequestViewModel,
                requestId = id,
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
                onUserRequestClick = {userId,sid ->
                    navController.navigate(
                        route = "${"alldetail"}/$userId/$sid "
                    )
                },
                readAllUserRequestsViewModel
            )
        }

        composable(
            route="${"allOffers"}/{sid}",
            arguments = listOf(navArgument("requestId"){
                NavType.StringType
                defaultValue=""
            })
        ){backStackEntry ->
            val sid = backStackEntry.arguments?.getString("sid") ?: ""
            val readUserRequestViewModel:ReadUserRequestViewModel = hiltViewModel()
            ReadAllOffersScreen(
                sid = sid,
                navigateUp = {navController.navigateUp()}


            )

        }




    }
}

//fun NavHostController.navigateToSingleTop(route:String){
//    navigate(route){
//        popUpTo(graph.findStartDestination().id){saveState=true}
//        launchSingleTop = true
//        restoreState = true
//    }
//}