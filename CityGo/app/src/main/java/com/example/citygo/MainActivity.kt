package com.example.citygo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.citygo.ui.theme.CityGoTheme
import com.example.userrequest.create.CreateDetailsUserRequestScreen
import com.example.userrequest.create.CreatePictureUserRequestScreen
import com.example.userrequest.create.CreatePriceUserRequestScreen
import com.example.userrequest.create.CreateUserRequestViewModel
import com.example.userrequest.read.ReadUserRequestScreen
import com.example.userrequest.read.ReadUserRequestViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CityGoTheme {
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()
                Router(navController = navController)
            }
        }
    }
}

@Composable
fun Router(navController: NavHostController) {
    NavHost(navController = navController,
        startDestination = "picture") {

        composable("picture"){
            val listContactsViewModel: CreateUserRequestViewModel = hiltViewModel()
            CreatePictureUserRequestScreen(navController = navController,
                listContactsViewModel
            )
        }

        composable("details") {
            val createContactViewModel: CreateUserRequestViewModel = hiltViewModel()
            CreateDetailsUserRequestScreen(navController = navController,
                createContactViewModel)
        }

        composable("price"){
            val listContactsViewModel: CreateUserRequestViewModel = hiltViewModel()
            CreatePriceUserRequestScreen(navController = navController,
                listContactsViewModel
            )
        }

        composable("read"){
            val listContactsViewModel: ReadUserRequestViewModel = hiltViewModel()
            ReadUserRequestScreen(navController = navController,
                listContactsViewModel
            )
        }



    }


}