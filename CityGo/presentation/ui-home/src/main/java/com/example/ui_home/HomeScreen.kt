package com.example.ui_home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel
) {
    Scaffold(
        topBar = {

        }
    ) {

Home(navController = navController, homeViewModel = homeViewModel)

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navController:NavController,homeViewModel: HomeViewModel){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "CYGO",
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 60.sp),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp)
        )
        Text(
            text = "Personal service with satisfaction guarantee. Service realization ready right now!",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.LocationOn, contentDescription = null)
            Text(text = "Set your location", modifier = Modifier.padding(start = 8.dp))
        }

        AnimatedVisibility(visible = homeViewModel.getUserRole()=="Owner") {
        Card(
            onClick = { navController.navigate("picture")},
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(200.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Yellow
            )
        ) {
            Column(modifier = Modifier
                .padding(16.dp)
                .wrapContentHeight()) {
                Text(text = "Move and\n transport", fontWeight = FontWeight.Bold,fontSize = 20.sp)
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()) {
                    // First icon at (x1, y1)
                    Image(painter= painterResource(id = R.drawable.resource_package), contentDescription = "Icon 1", modifier = Modifier.offset(x = 5.dp, y = 16.dp))
                    // Second icon at (x2, y2)
                    Image(painter= painterResource(id = R.drawable.couch_and_lamp_1), contentDescription = "Icon 2", modifier = Modifier.offset(x = 70.dp, y = 26.dp))
                    // Third icon at (x3, y3)
                    Image(painter= painterResource(id = R.drawable.delivery_truck_1), contentDescription = "Icon 3", modifier = Modifier.offset(x = 195.dp, y = 30.dp))
                    // Fourth icon at (x4, y4)
                    Image(painter= painterResource(id = R.drawable.plant), contentDescription = "Icon 4", modifier = Modifier.offset(x = 130.dp, y = (-70).dp))
                    // Fifth icon at (x5, y5)
                    Icon(painter= painterResource(id = R.drawable.arrow_right_long), contentDescription = "Icon 5", modifier = Modifier.offset(x = 280.dp, y = (-40).dp))
                }
            }
        }}
        Divider(modifier = Modifier.padding(16.dp))
        Text(
            text = "How does it work?",
            style = TextStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp),
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 16.dp, bottom = 8.dp),
            color = Color.Gray
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(painter= painterResource(id = R.drawable.group_smile_camera), contentDescription = null,modifier = Modifier.size(85.dp, 65.dp))
            Text(text = "Take a picture, set a price and create job request in just a few clicks.", modifier = Modifier.padding(start = 8.dp))
        }
        Divider(modifier = Modifier.padding(16.dp))

        Text(
            text = "Local collaborations:",
            style = TextStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp),
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 16.dp, bottom = 8.dp),
            color = Color.Gray
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Icon(painter = painterResource(id = R.drawable.package_car), contentDescription = "example")
            Icon(painter = painterResource(id = R.drawable.package_car), contentDescription = "example")
            Icon(painter = painterResource(id = R.drawable.package_car), contentDescription = "example")
            Icon(painter = painterResource(id = R.drawable.package_car), contentDescription = "example")
        }
    }





}
