package com.example.ui_users.read

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ui_users.R
import kotlinx.coroutines.runBlocking


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun FormScreen(userViewModel: UserProfileViewModel, onButtonClicked: () -> Unit) {
    val context = LocalContext.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF0F0E6)
        )
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 10.dp)
        ) {
            Card(
                modifier = Modifier
                    .padding(15.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Row(modifier = Modifier.padding(12.dp)) {
                    Image(painter = painterResource(id = R.drawable.ellipse_601), contentDescription = "ellipse")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "New appplication")
                }
                // Text fields
                TextField(
                    value = userViewModel.spname,
                    onValueChange = { userViewModel.setSPName(it) },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.DarkGray,
                        disabledTextColor = Color.Transparent,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color.Gray,
                        unfocusedIndicatorColor = Color.LightGray,
                        disabledIndicatorColor = Color.Transparent
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = userViewModel.spsurname,
                    onValueChange = { userViewModel.setSPSurame(it) },
                    label = { Text("Surname") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.DarkGray,
                        disabledTextColor = Color.Transparent,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color.Gray,
                        unfocusedIndicatorColor = Color.LightGray,
                        disabledIndicatorColor = Color.Transparent
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = userViewModel.spemail,
                    onValueChange = { userViewModel.setSPEmail(it) },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.DarkGray,
                        disabledTextColor = Color.Transparent,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color.Gray,
                        unfocusedIndicatorColor = Color.LightGray,
                        disabledIndicatorColor = Color.Transparent
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                ShowDatePicker(context = context) { date ->
                    userViewModel.setDateOfBirth(date)
                }
                Spacer(modifier = Modifier.height(8.dp))
                // Date picker

                TextField(
                    value = userViewModel.address,
                    onValueChange = { userViewModel.setAddress(it) },
                    label = { Text("Address") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.DarkGray,
                        disabledTextColor = Color.Transparent,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color.Gray,
                        unfocusedIndicatorColor = Color.LightGray,
                        disabledIndicatorColor = Color.Transparent
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = userViewModel.zipCode,
                    onValueChange = { userViewModel.setZipCode(it) },
                    label = { Text("ZIP Code") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.DarkGray,
                        disabledTextColor = Color.Transparent,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color.Gray,
                        unfocusedIndicatorColor = Color.LightGray,
                        disabledIndicatorColor = Color.Transparent
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = userViewModel.city,
                    onValueChange = { userViewModel.setCity(it) },
                    label = { Text("City") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.DarkGray,
                        disabledTextColor = Color.Transparent,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color.Gray,
                        unfocusedIndicatorColor = Color.LightGray,
                        disabledIndicatorColor = Color.Transparent
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = userViewModel.country,
                    onValueChange = { userViewModel.setCountry(it) },
                    label = { Text("Country") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.DarkGray,
                        disabledTextColor = Color.Transparent,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color.Gray,
                        unfocusedIndicatorColor = Color.LightGray,
                        disabledIndicatorColor = Color.Transparent
                    )

                )
                Spacer(modifier = Modifier.height(22.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier
                    .padding(15.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {


                    Text(
                        text = "Take a happy selfie",
                        color = Color.Black, // Set your desired text color
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Light, // Customize font weight if needed
                        fontStyle = FontStyle.Normal// Customize font style (e.g., italic)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    ImageInput(text = "Selfie") { uri ->
                        // Handle the received URI here, for example, update selfiePicture state
                        userViewModel.setSelfiePicture(uri)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(modifier = Modifier.padding(10.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Photo of national ID or driver license",
                        color = Color.Black, // Set your desired text color
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Light, // Customize font weight if needed
                        fontStyle = FontStyle.Normal// Customize font style (e.g., italic)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceEvenly) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Spacer(modifier = Modifier.height(8.dp))
                            ImageInput(text = "ID Card Front") { uri ->
                                // Handle the received URI here, for example, update selfiePicture state
                                userViewModel.setIdCardFrontPicture(uri)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Frontside",
                                color = Color.Black, // Set your desired text color
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Light, // Customize font weight if needed
                                fontStyle = FontStyle.Normal// Customize font style (e.g., italic)
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Spacer(modifier = Modifier.size(8.dp))
                            ImageInput(text = "ID Card Back") { uri ->
                                // Handle the received URI here, for example, update selfiePicture state
                                userViewModel.setIdCardBackPicture(uri)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Backside",
                                color = Color.Black, // Set your desired text color
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Light, // Customize font weight if needed
                                fontStyle = FontStyle.Normal// Customize font style (e.g., italic)
                            )
                        }

                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(modifier = Modifier.padding(10.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Upload a photo of your vehicle",
                        color = Color.Black, // Set your desired text color
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Light, // Customize font weight if needed
                        fontStyle = FontStyle.Normal// Customize font style (e.g., italic)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    ImageInput(text = "Vehicle") { uri ->
                        // Handle the received URI here, for example, update selfiePicture state
                        userViewModel.setVehiclePicture(uri)
                    }
                }
            }

                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        runBlocking { userViewModel.createServiceProviderProfile() }
                        onButtonClicked()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp), // Make the button fill the maximum width
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Yellow, // Set background color to yellow
                        contentColor = Color.Black // Set text color to black
                    )
                ) {
                    Text(
                        text = "Submit",
                        style = TextStyle.Default.copy(fontSize = 20.sp)
                    )
                }


    }
}
}

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun PendingScreen(
    navController: NavController,
    userViewModel: UserProfileViewModel = hiltViewModel()
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (userViewModel.statusLiveData=="Pending"){
            Text(
                text = "Pending...",
                style = TextStyle(fontSize = 24.sp),
                textAlign = TextAlign.Center
            )
        }else if (userViewModel.statusLiveData=="Accepted"){
//            Text(
//                text = "Čestitamo, postali ste prijevoznik za CityGo",
//                style = TextStyle(fontSize = 24.sp),
//                textAlign = TextAlign.Center
//            )
//            Button(onClick = { navController.navigate("provider") }) {
//                
//            }
            
            CygoScreen(navController = navController)
        }else if (userViewModel.statusLiveData=="Denied"){
            Text(
                text = "Denied...",
                style = TextStyle(fontSize = 24.sp),
                textAlign = TextAlign.Center
            )

            Button(
                onClick = {  userViewModel.deleteServiceProviderApplication() },
                colors = ButtonDefaults.buttonColors(Color.Gray),

                ) {
                Text(text = "Uredu", color = Color.White)
            }

        }else{
            FormScreen(userViewModel = userViewModel) {
                userViewModel.setStatusLiveData("Pending")
            }
        }

    }
}

@Composable
fun CygoScreen(navController:NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Čestitamo, postali ste Cygo prijevoznik",
            color = Color.Black,
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Card( modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)
            .padding(6.dp)
            .background(Color.Yellow, shape = RoundedCornerShape(8.dp)),
            colors = CardDefaults.cardColors(
                containerColor = Color.Yellow)
            ) {
            Icon(
                // Replace with a truck icon
                painter = painterResource(id = R.drawable.img),
                contentDescription = "Truck Icon",
                tint = Color.Black
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Jednostavno nađite i dogovorite posao bez puno priče",
                fontSize = 18.sp,textAlign = TextAlign.Center

            )
            Spacer(modifier = Modifier.height(16.dp))
            // Steps
            for (i in 1..4) {
                Text(
                    text = "$i Your step description here",
                    fontSize = 16.sp,textAlign = TextAlign.Center

                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {  navController.navigate("provider") },
                colors = ButtonDefaults.buttonColors(Color.Gray),

            ) {
                Text(text = "KRENI!", color = Color.White)
            }
        }

    }
}