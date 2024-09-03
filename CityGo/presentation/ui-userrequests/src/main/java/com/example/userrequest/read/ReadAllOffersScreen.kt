package com.example.userrequest.read

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.userrequest.R
import com.hfad.model.Loading
import com.hfad.model.Triumph
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


private lateinit var context: Context
private lateinit var activity: Activity
private var navController: NavHostController? = null
private lateinit var viewModel: ReadUserRequestViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ReadAllOffersScreen(
    sid: String,
    navigateUp: () -> Unit,
    ViewModel: ReadUserRequestViewModel = hiltViewModel(),
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    context = LocalContext.current
    viewModel = ViewModel
    activity = ((LocalContext.current as? Activity)!!)

    val state = remember {
        mutableStateListOf(
            OfferListResponseModel(
                id = "",
                userRequestUUID = "",
                serviceProviderId = "",
                timeTable = "",
                status = "",
                price = 0,
                serviceProviderImage = "",
                serviceVehicleImage = "",
                serviceProviderName = "",
                serviceProviderStars = 0.0,
                serviceProviderPhone = null
            )
        )
    }
    var offers by remember { mutableStateOf<List<OfferListResponseModel>>(emptyList()) }
    var isAccepted by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getOffers(sid)

        viewModel.offers.observe(lifecycleOwner) { value ->
            when (value) {
                is Loading -> {}
                is Triumph -> {
                    state.clear()
                    value.data.forEach { offer ->
                        offers = value.data.filter { it.status != "Denied" }
                        isAccepted = offers.any { it.status == "Accepted" }

                    }
                }

                is Error -> {}
                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "") },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp)
        ) {
            if (isAccepted) {
                offers.filter { it.status == "Accepted" }.forEach { offer ->
                    ReadContactSelectedProvider(
                        providerName = offer.serviceProviderName,
                        offerDateTime = offer.timeTable ?: "",
                        price = offer.price.toString(),
                        providerImageRes = offer.serviceProviderImage,
                        providerRating = offer.serviceProviderStars.toFloat(),
                        phoneNumber = offer.serviceProviderPhone.toString()
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxHeight()
                ) {
                    itemsIndexed(
                        items = offers,
                        key = { _, offer -> offer.id }
                    ) { index, offer ->
                        OfferItem(
                            index = index,
                            offer = offer,
                            sid = sid,
                            viewModel = viewModel,
                            onOfferStatusChanged = {
                                isAccepted = true
                                offers = offers.map {
                                    if (it.id == offer.id) it.copy(status = "Accepted") else it
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfferItem(
    index: Int,
    sid: String,
    offer: OfferListResponseModel,
    viewModel: ReadUserRequestViewModel,
    onOfferStatusChanged: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var isNewOfferAccepted by remember { mutableStateOf(false) }
    var isToggleButtonSelected by remember { mutableStateOf(false) }
    var price by remember { mutableStateOf(0) }
    val isEvenIndex = index % 2 == 0

    LaunchedEffect(Unit) {
        price = viewModel.getUserRequestPrice(sid)
    }

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
                    AsyncImage(
                        model = offer.serviceProviderImage.toUri(),
                        contentDescription = "Service provider image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(percent = 10))
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = "Image",
                        modifier = Modifier.fillMaxSize()
                    )
                }


                if (offer.serviceVehicleImage == null) {
                    Image(
                        painter = painterResource(R.drawable.user),
                        contentDescription = "avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.LightGray, CircleShape)
                            .align(Alignment.BottomEnd)
                    )
                } else {
                    Image(
                        painter = rememberAsyncImagePainter(offer.serviceVehicleImage),
                        contentDescription = "user vehicle",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.LightGray, CircleShape)
                            .align(Alignment.BottomEnd)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(8.dp)

            ) {
                // Timetable
                Text(text = offer.serviceProviderName, color = Color.Black)

                Spacer(modifier = Modifier.height(8.dp))
                if (offer.price!! != price) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.group_469),
                                contentDescription = stringResource(id = R.string.equalPrice),
                                modifier = Modifier.size(34.dp)

                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = stringResource(id = R.string.doJobFor) + offer.price.toString() + " €")
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
                                    onClick = {
                                        isNewOfferAccepted = true
                                        isVisible.value = false
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
                                ) {
                                    Text(
                                        stringResource(id = R.string.acceptText),
                                        color = Color.Black
                                    )
                                }
                                Button(
                                    onClick = {
                                        coroutineScope.launch {
                                            viewModel.updateOfferStatus(
                                                sid,
                                                "Denied"
                                            )
                                            isVisible.value = false
                                        }

                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                                ) {
                                    Text(
                                        stringResource(id = R.string.denyText),
                                        color = Color.White
                                    )
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
                                Text(
                                    stringResource(id = R.string.acceptedtext),
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }
            }
            Row {


                Column(modifier = Modifier.padding(vertical = 15.dp)) {
                    val currentDateTime = LocalDateTime.now(ZoneId.of("CET"))
                    val currentDate = currentDateTime.toLocalDate()
                    if (offer.timeTable?.substringAfter(",").equals(
                            currentDate.format(
                                DateTimeFormatter.ofPattern("dd/MM/yyyy")
                            )
                        )
                    ) {
                        Text(text = stringResource(id = R.string.today_title))
                    } else {
                        Text(text = stringResource(id = R.string.tommorow_title))
                    }

                    val customIconToggleButtonColors =
                        IconButtonDefaults.iconToggleButtonColors(
                            containerColor = Color.LightGray,
                            checkedContainerColor = Color.Yellow,
                            contentColor = Color.Black,
                            checkedContentColor = Color.Black
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
                        Text(text = stringResource(id = R.string.setTimetable))
                    }
                    FilledIconToggleButton(
                        checked = isToggleButtonSelected,
                        enabled = if (offer.price!! != price) isNewOfferAccepted else true,
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
                onClick = {
                    coroutineScope.launch {
                        try {
                            viewModel.denyOtherOffers(sid, offer.id)
                            viewModel.updateOfferStatus(offer.id, "Accepted")
                            onOfferStatusChanged()
                        } catch (e: Exception) {
                            Log.e("OfferItem", "Error accepting offer: ${e.message}")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = isToggleButtonSelected,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Yellow,
                    contentColor = Color.Black
                )
            ) {
                Text(text = stringResource(id = R.string.choose_cygo_text))
            }
        }
    }
}


