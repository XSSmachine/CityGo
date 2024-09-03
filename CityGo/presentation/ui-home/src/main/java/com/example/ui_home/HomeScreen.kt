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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel
) {

    val userRole by homeViewModel.userRole.collectAsState()
    LaunchedEffect(Unit) {
        homeViewModel.getUserRole()
    }
    Scaffold(
        topBar = {
            // Your top bar content
        }
    ) {

            Home(navController = navController,userRole)

    }
}

@Composable
fun Int.responsiveWidth(): Dp {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    return (this * screenWidth.value / 100).dp
}

@Composable
fun Int.responsiveHeight(): Dp {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    return (this * screenHeight.value / 100).dp
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navController:NavController,userRole:String){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "CYGO",
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 60.sp),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 2.responsiveHeight())
        )
        Text(
            text = stringResource(id = R.string.home_desciption_placeholder),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 1.responsiveHeight()),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(3.responsiveHeight()))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.LocationOn, contentDescription = null)
            Text(text = stringResource(id = R.string.home_location_dummy), modifier = Modifier.padding(start = 2.responsiveWidth()))
        }

        AnimatedVisibility(visible = userRole=="Owner") {
        Card(
            onClick = { navController.navigate("picture")},
            modifier = Modifier
                .padding(2.responsiveHeight())
                .fillMaxWidth()
                .height(27.responsiveHeight()),
            elevation = CardDefaults.cardElevation(4.dp),
            shape = RoundedCornerShape(3.responsiveHeight()),
            colors = CardDefaults.cardColors(
                containerColor = Color.Yellow
            )
        ) {
            Column(modifier = Modifier
                .padding(2.responsiveHeight())
                .wrapContentHeight()) {
                Text(text = stringResource(id = R.string.home_main_card), fontWeight = FontWeight.Bold,fontSize = 20.sp)
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()) {
                    // First icon at (x1, y1)
                    Image(painter= painterResource(id = R.drawable.resource_package), contentDescription = "Icon 1", modifier = Modifier.offset(x = 2.responsiveWidth(), y = 2.responsiveHeight()))
                    // Second icon at (x2, y2)
                    Image(painter= painterResource(id = R.drawable.couch_and_lamp_1), contentDescription = "Icon 2", modifier = Modifier.offset(x = 14.responsiveWidth(), y = 4.responsiveHeight()))
                    // Third icon at (x3, y3)
                    Image(painter= painterResource(id = R.drawable.delivery_truck_1), contentDescription = "Icon 3", modifier = Modifier.offset(x = 46.responsiveWidth(), y = 4.responsiveHeight()))
                    // Fourth icon at (x4, y4)
                    Image(painter= painterResource(id = R.drawable.plant), contentDescription = "Icon 4", modifier = Modifier.offset(x = 26.responsiveWidth(), y = -8.responsiveHeight()))
                    // Fifth icon at (x5, y5)
                    Icon(painter= painterResource(id = R.drawable.arrow_right_long), contentDescription = "Icon 5", modifier = Modifier.offset(x = 70.responsiveWidth(), y = -5.responsiveHeight()))
                }
            }
        }}
        Divider(modifier = Modifier.padding(16.dp))
        Text(
            text = stringResource(id = R.string.home_guide_title),
            style = TextStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp),
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 4.responsiveWidth(), bottom = 2.responsiveHeight()),
            color = Color.Gray
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(painter= painterResource(id = R.drawable.group_smile_camera), contentDescription = null,modifier = Modifier.size(85.dp, 65.dp))
            Text(text = stringResource(id = R.string.home_guide_text), modifier = Modifier.padding(start = 8.dp))
        }
        Divider(modifier = Modifier.padding(16.dp))

        Text(
            text = stringResource(id = R.string.home_collaboration_section),
            style = TextStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp),
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 16.dp, bottom = 8.dp),
            color = Color.Gray
        )
        SponsorRow()
    }
}
@Composable
fun SponsorImage(imageRes: Int, size: Dp) {
    Image(
        painter = painterResource(id = imageRes),
        contentDescription = "Sponsor logo",
        modifier = Modifier
            .size(size)
            .aspectRatio(1f), // This ensures a square aspect ratio
        contentScale = ContentScale.Fit // This scales the image to fit within the given size
    )
}
@Composable
fun SponsorRow() {
    val imageSize = 60.dp
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        SponsorImage(imageRes = R.drawable.fzoeu, size = 75.dp)
        SponsorImage(imageRes = R.drawable.eko_zadar, size = imageSize)
        SponsorImage(imageRes = R.drawable.prijatelj, size = imageSize)
        SponsorImage(imageRes = R.drawable.cistoca, size = 75.dp)
    }

}