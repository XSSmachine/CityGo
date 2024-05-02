package com.example.userrequest.read

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.userrequest.R
import com.example.userrequest.readAll.ReadAllUserRequestsViewModel
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ReadAllOffersScreen(
    navController: NavController,
    requestId:Long,
    navigateUp:() ->Unit,
    listUserRequestViewModel: ReadUserRequestViewModel = hiltViewModel()
) {

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit,
        block = {
            listUserRequestViewModel.getOffers(requestId)
        })
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "")
                },
                navigationIcon = { IconButton(onClick = navigateUp) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                }
            )
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            LazyColumn(
                modifier = Modifier.fillMaxHeight()
            ) {
                itemsIndexed(listUserRequestViewModel.offers) { index, item ->

                    OfferItem(index = index, offer = item, requestId = requestId, viewModel = listUserRequestViewModel)

                }
            }


        }

    }

}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun OfferItem(
//    index: Int,
//    offer: OfferListResponseModel,
//) {
//    var isToggleButtonSelected by remember { mutableStateOf(false) }
//    val isEvenIndex = index % 2 == 0
//    val shape = when {
//        isEvenIndex -> {
//            RoundedCornerShape(
//                topStart = 50f,
//                bottomEnd = 50f
//            )
//        }
//        else -> {
//            RoundedCornerShape(
//                topEnd = 50f,
//                bottomStart = 50f
//            )
//        }
//    }
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(top = 50.dp),
//        shape = shape,
//        onClick = { }
//    ) {
//        Column(
//            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
//            horizontalAlignment = Alignment.Start
//        ) {
//
//            Row (modifier = Modifier.padding(4.dp), verticalAlignment = Alignment.Top){
//
//                Text(text = "Stars")
//            }
//            // Picture
//            Box(
//                modifier = Modifier
//                    .size(150.dp)
//                    .padding(8.dp)
//            ) {
//                if (offer.serviceProviderImage != null) {
//                    Image(
//                        painter = rememberAsyncImagePainter(offer.serviceProviderImage),
//                        contentDescription = "Offer Image",
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .clip(shape = RoundedCornerShape(8.dp))
//                    )
//                } else {
//                    Image(
//                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
//                        contentDescription = "Default Image",
//                        modifier = Modifier.fillMaxSize()
//                    )
//                }
//            }
//
//            // Texts
//            Column(
//                modifier = Modifier
//                    .padding(8.dp)
//
//            ) {
//                // Name
//                Text(text = offer.serviceProviderName)
//
//                // Timetable
//                Row(
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Icon(
//                        painter = painterResource(id = R.drawable.baseline_access_time_filled_24),
//                        contentDescription = "Timetable",
//                        modifier = Modifier.size(16.dp)
//                    )
//                    Spacer(modifier = Modifier.width(4.dp))
//                    Text(text = offer.timeTable ?: "No timetable available")
//                }
//            }
//        }
//    }
//}



@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfferItem(
    index: Int,
    requestId: Long,
    offer: OfferListResponseModel,
    viewModel: ReadUserRequestViewModel
) {
    var isNewOfferAccepted by remember { mutableStateOf(false) }
    var isToggleButtonSelected by remember { mutableStateOf(false) }
    val isEvenIndex = index % 2 == 0
    val shape = when {
        isEvenIndex -> {
            RoundedCornerShape(
                topStart = 50f,
                bottomEnd = 50f
            )
        }
        else -> {
            RoundedCornerShape(
                topEnd = 50f,
                bottomStart = 50f
            )
        }
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp),
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(13.dp),
        onClick = { }
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Picture
            Card(
                modifier = Modifier
                    .padding(25.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Black
                )
            )
            {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Review rating",
                        modifier = Modifier.size(16.dp),
                        tint = Color(0xFFFFD700)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = offer.serviceProviderStars.toString(), color = Color.White)
                }

            }
            Box(
                modifier = Modifier
                    .size(170.dp)
                    .padding(8.dp)
            ) {
                if (offer.serviceProviderImage != null) {
                    Image(
                        painter = rememberAsyncImagePainter(offer.serviceProviderImage),
                        contentDescription = "Image",
                        modifier = Modifier
                            .size(125.dp)
                            .clip(shape = RoundedCornerShape(8.dp))
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = "Image",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Image(
                    painter = painterResource(R.drawable.user),
                    contentDescription = "avatar",
                    contentScale = ContentScale.Crop,            // crop the image if it's not a square
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.LightGray, CircleShape)// clip to the circle shape
                        .align(Alignment.BottomEnd)// add a border (optional)
                )

            }


            // Texts
            Column(
                modifier = Modifier
                    .padding(8.dp)

            ) {
                // Timetable
                Text(text = offer.serviceProviderName, color = Color.Black)
                Log.d("PROVIDER", offer.serviceProviderName)

                Spacer(modifier = Modifier.height(8.dp))
                if (offer.price!! != viewModel.getUserRequestById(requestId.toInt())?.price!!) {


                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.group_469),
                                contentDescription = "Equal price and offer",
                                modifier = Modifier.size(34.dp)

                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = "Will do the job for " + offer.price.toString() + " €")
                        }
                        val isVisible = remember { mutableStateOf(true) }


                        AnimatedVisibility(
                            visible = isVisible.value,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {

                                    Button(
                                        onClick = { isNewOfferAccepted = true
                                            isVisible.value = false },
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
                                    ) {
                                        Text("Accept", color = Color.Black)
                                    }
                                    Button(
                                        onClick = { viewModel.updateOfferStatus(requestId.toInt(), offer.serviceProviderId, "Denied")
                                            isVisible.value = false },
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                                    ) {
                                        Text("Deny", color = Color.White)
                                    }

                            }
                        }
                        AnimatedVisibility(visible = isNewOfferAccepted) {
                            Button(
                                onClick = { /* TODO: Handle click */ },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(imageVector = Icons.Default.Check, contentDescription = "Done")
                                Text("Accepted", color = Color.Black)
                            }
                        }

//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        modifier = Modifier.padding(top = 20.dp)
//                    ) {
//                        Log.d("OFFER",offer.price.toString())
//                        Log.d("REQUEST",viewModel.getUserRequestById(requestId.toInt())?.price.toString())
//                        if (offer.price!! > viewModel.getUserRequestById(requestId.toInt())?.price!!){
//                            Icon(
//                                painter = painterResource(id = R.drawable.transfer_top),
//                                contentDescription = "Equal price and offer",
//                                modifier = Modifier.size(16.dp), tint = Color(0xFF008080)
//
//                            )
//                        }else if (offer.price!! < viewModel.getUserRequestById(requestId.toInt())?.price!!){
//                            Icon(
//                                painter = painterResource(id = R.drawable.transfer_long_down),
//                                contentDescription = "Equal price and offer",
//                                modifier = Modifier.size(16.dp), tint = Color(0xFF008080)
//
//                            )
//                        }else {
//                            Icon(
//                                painter = painterResource(id = R.drawable.baseline_done_all_24),
//                                contentDescription = "Equal price and offer",
//                                modifier = Modifier.size(16.dp), tint = Color(0xFF008080)
//
//                            )
//
//                        }
//                        Spacer(modifier = Modifier.width(4.dp))
//                        Text(text = offer.price.toString() + " €")
//                    }
                    }
                }

                Row {


                    Column(modifier = Modifier.padding(vertical = 15.dp)) {
                        val currentDateTime = LocalDateTime.now(ZoneId.of("CET"))

                        // Get current date and next day
                        val currentDate = currentDateTime.toLocalDate()
                        if (offer.timeTable?.substringAfter(",").equals(
                                currentDate.format(
                                    DateTimeFormatter.ofPattern("dd/MM/yyyy")
                                )
                            )
                        ) {
                            Text(text = "Today")
                        } else {
                            Text(text = "Tommorow")
                        }

                        val customIconToggleButtonColors =
                            IconButtonDefaults.iconToggleButtonColors(
                                containerColor = Color.LightGray, // Background color when unchecked
                                checkedContainerColor = Color.Yellow, // Background color when checked
                                contentColor = Color.Black // Color of the content (icon and text)
                                , checkedContentColor = Color.Black
                            )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 20.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_access_time_filled_24),
                                contentDescription = "Timetable",
                                modifier = Modifier.size(16.dp), tint = Color(0xFF008080)

                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Select time slot")
                        }
                        FilledIconToggleButton(
                            checked = isToggleButtonSelected,
                            enabled = if (offer.price!! != viewModel.getUserRequestById(requestId.toInt())?.price!!) isNewOfferAccepted else true,
                            onCheckedChange = { isChecked ->
                                isToggleButtonSelected = isChecked
                            },
                            modifier = Modifier
                                .padding(vertical = 3.dp, horizontal = 13.dp)
                                .size(65.dp, 35.dp), colors = customIconToggleButtonColors
                        ) {
                            Text(text = offer.timeTable?.substringBefore(",").toString())
                        }
                    }

                }

                Spacer(modifier = Modifier.height(4.dp))

                // Button
                Button(
                    onClick = { /* Change status of the offer and navigate to success screen */ },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isToggleButtonSelected,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Yellow,
                        contentColor = Color.Black
                    )
                ) {
                    Text(text = "Choose cygo")
                }


            }
        }
    }
}


