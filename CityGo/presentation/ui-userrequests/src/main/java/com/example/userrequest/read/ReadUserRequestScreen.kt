package com.example.userrequest.read

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.userrequest.R
import com.example.userrequest.create.responsiveHeight
import com.example.userrequest.create.responsiveWidth
import com.hfad.model.Address
import com.hfad.model.Loading
import com.hfad.model.OfferResponseModel
import com.hfad.model.Triumph
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private lateinit var context: Context
private lateinit var activity: Activity
private var navController: NavHostController? = null
private lateinit var viewModel: ReadUserRequestViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation")
@Composable
fun ReadUserRequestScreen(
    navController: NavController,
    onUserRequestClick: (String, String) -> Unit,
    onUserRequestButtonClick: (String) -> Unit,
    onCygoOfferClick: (String, String) -> Unit,
    ViewModel: ReadUserRequestViewModel = hiltViewModel(),
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    context = LocalContext.current
    val scope = rememberCoroutineScope()
    viewModel = ViewModel
    activity = ((LocalContext.current as? Activity)!!)

    val role = remember { mutableStateOf("") }
    val userRequests = remember {
        mutableStateListOf(
            UserRequestListResponseModel(
                uuid = "",
                userId = "",
                photo = "".toUri(),
                description = "",
                address1 = Address("123 Main St", null, null, false, 0, "", ""),
                address2 = Address("123 Main St", null, null, false, 0, "", ""),
                timeTable = "",
                category = "",
                extraWorker = false,
                price = 0,
                sid = "",
                status = "",
                offerPrice = 0
            )
        )
    }

    val SPuserRequests =
        remember {
            mutableStateListOf(
                UserRequestListResponseModel(
                    uuid = "",
                    userId = "",
                    photo = "".toUri(),
                    description = "",
                    address1 = Address("123 Main St", null, null, false, 0, "", ""),
                    address2 = Address("123 Main St", null, null, false, 0, "", ""),
                    timeTable = "",
                    category = "",
                    extraWorker = false,
                    price = 0,
                    sid = "",
                    status = "",
                    offerPrice = 0
                )
            )
        }


    LaunchedEffect(Unit) {
        role.value = viewModel.getUserRole()
        if (viewModel.getUserRole() == "Cygo") {
            viewModel.getServiceProviderOffers()
        } else {
            viewModel.getMyUserRequest()
        }
        viewModel.userRequests.observe(lifecycleOwner) { value ->
            when (value) {
                is Loading -> {}
                is Triumph -> {
                    when (value.data) {
                        is List<UserRequestListResponseModel> -> {
                            userRequests.clear()
                            userRequests.addAll(value.data)
                        }
                    }
                }

                is Error -> {}
                else -> {}
            }
        }

        viewModel.spUserRequests.observe(lifecycleOwner) { value ->
            when (value) {
                is Loading -> {}
                is Triumph -> {
                    when (value.data) {
                        is List<UserRequestListResponseModel> -> {
                            SPuserRequests.clear()
                            SPuserRequests.addAll(value.data)
                        }
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
                title = {
                    Text(text = stringResource(id = R.string.my_activity))
                },

                )
        }
    ) {
        SideEffect {
            role.value = viewModel.getUserRole()
            if (role.value == "Cygo") {
                scope.launch {
                    viewModel.getServiceProviderOffers()
                }
            } else {
                scope.launch {
                    viewModel.getMyUserRequest()
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(top = 50.dp)
            ) {
                if (viewModel.getUserRole() == "Owner") {
                    if (userRequests.isEmpty() || userRequests.all { it.userId == "" }) {
                        item {
                            Box(modifier = Modifier.fillParentMaxSize()) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.empty_box), // Replace with your image resource
                                        contentDescription = "No data image",
                                        modifier = Modifier.size(25.responsiveHeight()) // Adjust size as needed
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(text = stringResource(id = R.string.no_data_text))
                                }
                            }
                        }
                    } else {
                        itemsIndexed(userRequests) { index, item ->

                            UserRequestItem(
                                index = index,
                                userRequest = item,
                                onUserRequestClicked = onUserRequestClick,
                                onUserRequestButtonClicked = onUserRequestButtonClick,
                                onCygoOfferClick = onCygoOfferClick,
                                viewModel = viewModel,
                                lifecycleOwner = lifecycleOwner
                            )
                        }
                    }
                } else if (viewModel.getUserRole() == "Cygo") {
                    if (SPuserRequests.isEmpty() || SPuserRequests.all { it.userId == "" }) {
                        item {
                            Box(modifier = Modifier.fillParentMaxSize()) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.empty_box), // Replace with your image resource
                                        contentDescription = "No data image",
                                        modifier = Modifier.size(25.responsiveHeight()) // Adjust size as needed
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(text = stringResource(id = R.string.no_data_text))
                                }
                            }
                        }
                    } else {
                        itemsIndexed(SPuserRequests) { index, item ->
                            UserRequestItem(
                                index = index,
                                userRequest = item,
                                onUserRequestClicked = onUserRequestClick,
                                onUserRequestButtonClicked = onUserRequestButtonClick,
                                onCygoOfferClick = onCygoOfferClick,
                                viewModel = viewModel,
                                lifecycleOwner = lifecycleOwner
                            )
                        }
                    }
                }
            }
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserRequestItem(
    index: Int,
    userRequest: UserRequestListResponseModel,
    onUserRequestClicked: (String, String) -> Unit,
    onUserRequestButtonClicked: (String) -> Unit,
    onCygoOfferClick: (String, String) -> Unit,
    viewModel: ReadUserRequestViewModel,
    lifecycleOwner: LifecycleOwner
) {

    val isLoading = remember { mutableStateOf(true) }
    var trueFalse by remember(userRequest.sid) { mutableStateOf(false) }
    var offer by remember(userRequest.sid) {
        mutableStateOf(
            OfferResponseModel(
                id = 0,
                price = 0,
                serviceProviderId = "",
                sid = "",
                status = "",
                sync = null,
                timeTable = "",
                userRequestUUID = ""
            )
        )
    }

    val isEvenIndex = index % 2 == 0
    val cardColor = if (isEvenIndex) Color(0xFFFFF9C4) else Color(0xFFE1F5FE)
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

    LaunchedEffect(userRequest.sid) {

        viewModel.getSingleOffer(userRequest.sid)
        if (!userRequest.sid.isNullOrEmpty()) {

            Log.d("TRUE/FALSE", viewModel.checkOffersForRequest(userRequest.sid).toString())
            trueFalse = viewModel.checkOffersForRequest(userRequest.sid)


        }

        isLoading.value = false



        viewModel.singleOffer.observe(lifecycleOwner) { value ->
            when (value) {
                is Loading -> {
                    //Loading case
                }

                is Triumph -> {
                    when (value.data) {
                        is OfferResponseModel -> {

                            offer = value.data

                        }

                    }
                }

                is Error -> {
                    Log.d("userOffer-Error", value.toString())

                }

                else -> {}
            }
        }

    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.responsiveWidth(), vertical = 1.responsiveHeight()),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.responsiveHeight()),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = cardColor),
        onClick = {
            if (viewModel.getUserRole() == "Owner")
                onUserRequestClicked(userRequest.uuid, userRequest.userId)
            else
                onCygoOfferClick(userRequest.userId, userRequest.sid)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.responsiveWidth())
        ) {
            // Offer data section
            if (viewModel.getUserRole() == "Cygo" && !isLoading.value) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = stringResource(id = R.string.my_offer),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Status: ${userRequest.status}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = "Price: ${userRequest.offerPrice} €",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    AnimatedVisibility(visible = userRequest.status == "Pending") {
                        IconButton(
                            onClick = {
                                runBlocking {
                                    viewModel.deleteOffer(offer.sid!!)
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Offer",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }

                }
                Divider(modifier = Modifier.padding(vertical = 1.responsiveHeight()))
            }

            // User request data section
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Image
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(userRequest.photo ?: R.drawable.ic_launcher_foreground)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(20.responsiveWidth())
                        .clip(RoundedCornerShape(2.responsiveWidth()))
                )

                Spacer(modifier = Modifier.width(2.responsiveWidth()))

                // User request details
                Column {
                    // Timetable
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Timetable",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(3.responsiveWidth())
                        )
                        Spacer(modifier = Modifier.width(1.responsiveWidth()))
                        Text(
                            text = formatTimeTable(userRequest.timeTable),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(1.responsiveHeight()))

                    // Address
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Address",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(3.responsiveWidth())
                        )
                        Spacer(modifier = Modifier.width(1.responsiveWidth()))
                        Text(
                            text = "${userRequest.address1.addressName} | ${userRequest.address2.addressName}",
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.height(1.responsiveHeight()))

                    // Price
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Price",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(3.responsiveWidth())
                        )
                        Spacer(modifier = Modifier.width(1.responsiveWidth()))
                        Text(
                            text = stringResource(id = R.string.total_amount) + " ${userRequest.price}€",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(1.responsiveHeight()))

                    // Button
                    if (isLoading.value) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(3.responsiveWidth()),
                            color = MaterialTheme.colorScheme.secondary
                        )
                    } else if (viewModel.getUserRole() == "Owner") {
                        if (!trueFalse) {
                            Button(
                                onClick = { /* update offer price by 5 euro */ },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                                modifier = Modifier.height(5.responsiveHeight())
                            ) {
                                Text(text = "+5€", style = MaterialTheme.typography.labelMedium)
                            }
                        } else {
                            Button(
                                onClick = { onUserRequestButtonClicked(userRequest.sid) },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                modifier = Modifier.height(5.responsiveHeight())
                            ) {
                                Text(
                                    text = stringResource(id = R.string.select_cygo_text),
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


fun formatTimeTable(timeTable: String): String {
    val parts = timeTable.split(",")
    if (parts.size >= 2) {
        val time = parts[0].trim()
        val date = parts[1].trim()
        return "$time, $date"
    }
    return timeTable
}


