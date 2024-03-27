package com.example.citygo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.citygo.components.AppContent
import com.example.citygo.navigation.Router
import com.example.citygo.ui.theme.CityGoTheme
import com.example.ui_users.login.CreateUserDialogScreen
import com.example.ui_users.login.UserLoginViewModel
import com.example.ui_users.login.LoginScreen
import com.example.userrequest.create.CreateDetailsUserRequestScreen
import com.example.userrequest.create.CreatePictureUserRequestScreen
import com.example.userrequest.create.CreatePriceUserRequestScreen
import com.example.userrequest.create.CreateUserRequestViewModel
import com.example.userrequest.read.ReadUserRequestScreen
import com.example.userrequest.read.ReadUserRequestViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CityGoTheme {
                // A surface container using the 'background' color from the theme
                AppContent()
            }
        }
    }
}

//@Composable
//fun Router(navController: NavHostController,
//           createUserProfileViewModel: UserLoginViewModel,
//           createContactViewModel: CreateUserRequestViewModel
//           ) {
//    NavHost(navController = navController,
//        startDestination = "login") {
//
//        composable("login"){
//
//            LoginScreen(navController = navController,
//                createUserProfileViewModel
//            )
//        }
//        composable("dialog"){
//
//            CreateUserDialogScreen(navController = navController,
//                createUserProfileViewModel
//            )
//        }
//
//        composable("picture"){
//
//            CreatePictureUserRequestScreen(navController = navController,
//                createContactViewModel
//            )
//        }
//
//        composable("details") {
//
//            CreateDetailsUserRequestScreen(navController = navController,
//                createContactViewModel)
//        }
//
//        composable("price"){
//
//            CreatePriceUserRequestScreen(navController = navController,
//                createContactViewModel
//            )
//        }
//
//        composable("read"){
//            val listContactsViewModel: ReadUserRequestViewModel = hiltViewModel()
//            ReadUserRequestScreen(navController = navController,
//                listContactsViewModel
//            )
//        }
//
//
//
//    }
//}