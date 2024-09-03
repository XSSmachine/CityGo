package com.example.ui_users.read

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ui_users.R
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun FormScreen(userViewModel: UserProfileViewModel, onButtonClicked: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
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
                    Text(text = stringResource(id = R.string.new_application_title))
                }
                // Text fields
                TextField(
                    value = userViewModel.spname,
                    onValueChange = { userViewModel.setSPName(it) },
                    label = { Text(stringResource(id = R.string.name_text)) },
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
                    label = { Text(stringResource(id = R.string.surname_text)) },
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
                    label = { Text(stringResource(id = R.string.email_text)) },
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
                    label = { Text(stringResource(id = R.string.address_text)) },
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
                    label = { Text(stringResource(id = R.string.zip_text)) },
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
                    label = { Text(stringResource(id = R.string.city_text)) },
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
                    label = { Text(stringResource(id = R.string.country_text)) },
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
                        text = stringResource(id = R.string.happy_selfie_text),
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
                        text = stringResource(id = R.string.id_picture_text),
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
                                userViewModel.setIdCardFrontPicture(uri)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = stringResource(id = R.string.id_front),
                                color = Color.Black, // Set your desired text color
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Light, // Customize font weight if needed
                                fontStyle = FontStyle.Normal// Customize font style (e.g., italic)
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Spacer(modifier = Modifier.size(8.dp))
                            ImageInput(text = "ID Card Back") { uri ->
                                userViewModel.setIdCardBackPicture(uri)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = stringResource(id = R.string.id_back),
                                color = Color.Black,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Light,
                                fontStyle = FontStyle.Normal
                            )
                        }

                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(modifier = Modifier.padding(10.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(id = R.string.vehicle_picture),
                        color = Color.Black,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Light,
                        fontStyle = FontStyle.Normal
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    ImageInput(text = "Vehicle") { uri ->
                        userViewModel.setVehiclePicture(uri)
                    }
                }
            }

                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                         scope.launch{ userViewModel.createServiceProviderProfile() }
                        onButtonClicked()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Yellow,
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.btn_submit),
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

    val status by userViewModel.statusLiveData.collectAsState(initial = "")

    LaunchedEffect(Unit) {
        userViewModel.refreshStatus()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (status) {
            "Pending" -> PendingContent(userViewModel)
            "Accepted" -> AcceptedContent(navController)
            "Denied" -> DeniedContent(userViewModel)
            else -> FormScreen(userViewModel) {
                userViewModel.setStatusLiveData("Pending")
            }
        }
    }
}

@Composable
fun PendingContent(userViewModel: UserProfileViewModel) {
    var isRefreshing by remember { mutableStateOf(false) }
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = {
            isRefreshing = true
            userViewModel.refreshStatus()
        }
    ) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            CircularProgressIndicator(
                color = Color.Yellow,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.pending_text),
                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.review_application_text),
                style = TextStyle(fontSize = 16.sp, color = Color.Gray),
                textAlign = TextAlign.Center
            )
        }

    }
    }
    LaunchedEffect(userViewModel.statusLiveData) {
        isRefreshing = false
    }
}

@Composable
fun AcceptedContent(navController: NavController) {
    CygoScreen(navController = navController)
}

@Composable
fun DeniedContent(userViewModel: UserProfileViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Denied",
            tint = Color.Red,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.denied_entry_text),
            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.denied_desc_text),
            style = TextStyle(fontSize = 16.sp, color = Color.Gray),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { userViewModel.deleteServiceProviderApplication() },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text(text = "Okay", color = Color.White)
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
            text = stringResource(id = R.string.cygo_congrats),
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
                text = stringResource(id = R.string.congrats_desc),
                fontSize = 18.sp,textAlign = TextAlign.Center

            )
            Spacer(modifier = Modifier.height(16.dp))
            // Steps
            for (i in 1..4) {
                Text(
                    text = "$i "+ stringResource(id = R.string.step_desc),
                    fontSize = 16.sp,textAlign = TextAlign.Center

                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {  navController.navigate("provider") },
                colors = ButtonDefaults.buttonColors(Color.Gray),

            ) {
                Text(text = stringResource(id = R.string.letsgo), color = Color.White)
            }
        }

    }
}