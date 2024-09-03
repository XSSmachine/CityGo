package com.example.userrequest.details

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.userrequest.R
import com.hfad.model.Address
import com.hfad.model.Loading
import com.hfad.model.Triumph
import com.hfad.model.UserProfileResponseModel
import com.hfad.model.UserRequestResponseModel
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


private lateinit var context: Context
private lateinit var activity: Activity
private var navController: NavHostController? = null
private lateinit var viewModel: DetailUserRequestViewModel

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@Composable
fun DetailScreen(
    ViewModel: DetailUserRequestViewModel,
    requestId: String,
    userId: String,
    navigateUp: () -> Unit,
    onUserRequestUpdate: (String, String) -> Unit,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

) {
    context = LocalContext.current
    viewModel = ViewModel

    activity = ((LocalContext.current as? Activity)!!)

    val userData = remember {
        mutableStateOf(
            UserProfileResponseModel(
                id = "",
                email = "",
                name = "",
                surname = "",
                stars = 0.0,
                profilePicture = "",
                phoneNumber = "",
                sid = null,
                sync = null,
                requests = null

            )
        )
    }

    val state = remember {
        mutableStateOf(
            UserRequestResponseModel(
                uuid = "",
                date = "",
                userId = "",
                photo = "".toUri(),
                description = "",
                address1 = Address("", null, null, true, 0, "", ""),
                address2 = Address("", null, null, true, 0, "", ""),
                timeTable = "",
                category = "",
                extraWorker = false,
                price = 0,
                sid = "",
                sync = null,
                offers = null
            )
        )
    }



    LaunchedEffect(Unit) {
        // Run on first screen compose


//

        viewModel.getUserRequest(requestId, userId)




        viewModel.getProfileDetails(userId)
        viewModel.requestViewState.observe(lifecycleOwner) { value ->
            Log.d("DETAIL_STATE", value.toString())
            when (value) {

                is Loading -> {


                }

                is Triumph -> {
                    when (value.data) {

                        is UserRequestResponseModel -> {

                            state.value = value.data
                        }

                    }
                }

                is Error -> {

                    Toast.makeText(context, "No user data", Toast.LENGTH_SHORT).show()
                }

                else -> {

                }
            }
        }

        viewModel.profileViewState.observe(lifecycleOwner) { value ->
            when (value) {
                is Loading -> {
                    //Loading case
                }

                is Triumph -> {
                    when (value.data) {
                        is UserProfileResponseModel -> {
                            userData.value = value.data
                        }

                    }
                }

                is Error -> {

                }

                else -> {}
            }
        }


    }

    Box(

        content = {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                Box() {
                    Image(
                        painter = rememberAsyncImagePainter(state.value.photo),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                    )

                    // TopAppBar

                    IconButton(onClick = navigateUp) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }



                Spacer(modifier = Modifier.height(16.dp))

                // Row with buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (viewModel.getIdValue() == userId) {
                        Button(
                            onClick = { onUserRequestUpdate(requestId, userId) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Yellow,
                                contentColor = Color.Black
                            )
                        ) {
                            Icon(Icons.Default.Create, contentDescription = "Update")
                        }
                    }

                    Button(
                        onClick = { /* Handle second button click */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Yellow,
                            contentColor = Color.Black
                        )
                    ) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Column with pickup time
                Row {
                    Image(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Pick-up time"
                    )
                    Text(text = stringResource(id = R.string.pickup_time))
                }
                Column {
                    val currentDateTime = LocalDateTime.now(ZoneId.of("CET"))

                    // Get current date and next day
                    val currentDate = currentDateTime.toLocalDate()
                    if (state.value.timeTable.substringAfter(",").equals(
                            currentDate.format(
                                DateTimeFormatter.ofPattern("dd/MM/yyyy")
                            )
                        )
                    ) {
                        Text(text = stringResource(id = R.string.today_title))
                    } else {
                        Text(text = stringResource(id = R.string.tommorow_title))
                    }

                    FilledIconToggleButton(
                        checked = false,
                        onCheckedChange = { //nothing
                        },
                        modifier = Modifier
                            .padding(vertical = 3.dp, horizontal = 13.dp)
                            .size(65.dp, 35.dp)
                    ) {
                        Text(text = state.value.timeTable.substringBefore(","))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Button filling whole row

                if (viewModel.getIdValue() == userId) {
                    Button(
                        onClick = {
                            viewModel.deleteUserRequest(requestId, userId)
                            navigateUp()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Yellow,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(text = stringResource(id = R.string.delete_request))
                    }
                }


//                CreateOfferButton(
//                    detailUserRequestViewModel = viewModel,
//                    navigateUp = { navigateUp },
//                    requestId = requestId,
//                    offerExist,
//                    state.value
//                )


                Spacer(modifier = Modifier.height(16.dp))

                // Separator
                Divider()

                Spacer(modifier = Modifier.height(16.dp))

                // Row to display price
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = stringResource(id = R.string.price_text))
                    Text(text = state.value.price.toString() + " €")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Separator with description text
                Divider()
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = stringResource(id = R.string.desc_text))
                OutlinedTextField(
                    value = state.value.description,
                    onValueChange = { /* Do nothing */ },
                    minLines = 3,
                    maxLines = 3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(6.dp),

                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedTextColor = Color.Black,
                        disabledTextColor = Color.Black, // Set the text color to black
                        disabledBorderColor = Color.LightGray,
                        disabledContainerColor = Color.LightGray,
                    ),
                    enabled = false
                )
                Spacer(modifier = Modifier.height(16.dp))


                // Row with some text
                Column {
                    if (state.value.extraWorker) {
                        Text(text = stringResource(id = R.string.worker_2))
                    } else {
                        Text(text = stringResource(id = R.string.worker_1))
                    }
                    Spacer(modifier = Modifier.height(6.dp))

                    Card {
                        if (state.value.extraWorker) {


                            Row {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_directions_run_24),
                                    contentDescription = "Worker"
                                )
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_directions_run_24),
                                    contentDescription = "Worker"
                                )
                            }
                        } else {
                            Row {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_directions_run_24),
                                    contentDescription = "Worker"
                                )
                            }
                        }
                    }

                }

                //Spacer(modifier = Modifier.height(16.dp))

                Image(
                    alignment = Alignment.Center,
                    painter = painterResource(id = R.drawable.google_maps),
                    contentDescription = "Google Map Window",
                    modifier = Modifier
                        .width(370.dp)
                        .height(300.dp) // Adjust the size as needed
                )

                //Spacer(modifier = Modifier.height(16.dp))

                // Custom composable for displaying 5 strings vertically
                state.value.address1.let { it1 ->
                    DisplayVerticalText(
                        text1 = it1.addressName,
                        text2 = it1.liftStairs,
                        text3 = it1.floor.toString(),
                        text4 = it1.doorCode.toString(),
                        text5 = it1.phoneNumber,
                        icon = R.drawable.baseline_rocket_launch_24,
                        desc = stringResource(id = R.string.pickup_address)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Divider()

                state.value.address2.let { it2 ->
                    DisplayVerticalText(
                        text1 = it2.addressName,
                        text2 = it2.liftStairs,
                        text3 = it2.floor.toString(),
                        text4 = it2.doorCode.toString(),
                        text5 = it2.phoneNumber,
                        icon = R.drawable.baseline_done_all_24,
                        desc = stringResource(id = R.string.delivery_address)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Divider()

                Row {
                    Text(
                        text = stringResource(id = R.string.owner_text),
                        style = TextStyle(
                            fontWeight = FontWeight.Light, // Set the font weight to bold
                            fontSize = 20.sp // Set the font size to 20sp (adjust as needed)
                        )
                    )

                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (userData.value.profilePicture.toString().isNullOrEmpty()) {
                        Image(
                            painter = painterResource(id = R.drawable.user),
                            contentDescription = "Default user image",
                            modifier = Modifier.size(80.dp)
                        )
                    } else {
                        AsyncImage(
                            model = userData.value.profilePicture?.toUri(),
                            contentDescription = "avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(percent = 10))
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = userData.value.name,
                            style = MaterialTheme.typography.headlineSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = userData.value.stars.toString(),
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            RatingBar(
                                rating = userData.value.stars.toFloat(),
                                spaceBetween = 4.dp
                            )
                        }
                    }
                }


            }
        }
    )

}

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Float,
    spaceBetween: Dp = 2.dp
) {
    val starIconEmpty = R.drawable.baseline_star_outline_24
    val starIconFull = R.drawable.baseline_star_24
    val starIconHalf = R.drawable.baseline_star_half_24

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spaceBetween)
    ) {
        repeat(5) { index ->
            val starRating = index + 1
            val icon = when {
                starRating <= rating -> starIconFull
                starRating - 0.5f <= rating -> starIconHalf
                else -> starIconEmpty
            }

            Icon(
                painter = painterResource(id = icon),
                contentDescription = "Star $starRating",
                tint = Color(0xFFFFD700), // Gold color
                modifier = Modifier.size(20.dp) // Adjust size as needed
            )
        }
    }
}


@Composable
fun CreateOfferButton(
    detailUserRequestViewModel: DetailUserRequestViewModel,
    navigateUp: () -> Unit,
    requestId: String,
    offerExist: Boolean,
    state: UserRequestResponseModel
) {
    var dialogOpen by remember { mutableStateOf(false) }

    if (detailUserRequestViewModel.getUserRole() == "Cygo") {
        Log.d("REQUESTID-", requestId.toString())
        if (offerExist) {

            Button(
                onClick = { dialogOpen = false },
                modifier = Modifier.fillMaxWidth(),
                enabled = false
            ) {
                Text(text = "Already sent application")
            }
        } else {
            Button(
                onClick = { dialogOpen = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Apply")
            }
        }
    }


    if (dialogOpen) {
        CreateOfferDialogScreen(
            detailUserRequestViewModel,
            navigateUp = navigateUp,
            requestId = requestId,
            state = state
        )
    }
}


@Composable
fun DisplayVerticalText(
    text1: String,
    text2: Boolean,
    text3: String,
    text4: String,
    text5: String,
    icon: Int,
    desc: String
) {
    Column {
        Row {
            Icon(painter = painterResource(id = icon), contentDescription = "address")
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = desc)
        }
        Spacer(modifier = Modifier.size(12.dp))
        Text(text = text1)
        Spacer(modifier = Modifier.size(7.dp))
        if (text2) {
            Text(text = "Lift")
        } else {
            Text(text = "Stairs")
        }

        Spacer(modifier = Modifier.size(7.dp))
        Text(text = "Floors: " + text3)
        Spacer(modifier = Modifier.size(7.dp))
        if (text4.isNullOrEmpty()) {
            Text(text = stringResource(id = R.string.emptyDoorCode))
        } else {
            Text(text = stringResource(id = R.string.descDoorCode) + text4)
        }

        Spacer(modifier = Modifier.size(7.dp))
        Row {
            Text(
                text = stringResource(id = R.string.descContact),
                style = TextStyle(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp
                )
            )
            Text(
                text = text5,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Blue
                )
            )
        }
    }
}

@Composable
fun CreateOfferDialogScreen(
    viewModel: DetailUserRequestViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
    requestId: String,
    state: UserRequestResponseModel
) {
    var showDialog by remember { mutableStateOf(true) }
    var price by remember { mutableStateOf(state.price.toString()) }
    var timeTable by remember { mutableStateOf(state.timeTable) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },
            title = { Text(stringResource(id = R.string.applyJob)) },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    OutlinedTextField(
                        value = price,
                        onValueChange = { price = it },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        label = { Text(stringResource(id = R.string.price_text)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = timeTable,
                        onValueChange = { timeTable = it },
                        label = { Text(stringResource(id = R.string.descTimetable)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.createOffer(
                            requestId,
                            price.toIntOrNull() ?: 0,
                            timeTable
                        )
                        navigateUp()
                        showDialog = false
                    },
                    enabled = price.isNotBlank() && (timeTable.isNotBlank() || state.timeTable.isNotBlank())
                ) {
                    Text(stringResource(id = R.string.confirmText))
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDialog = false
                    }
                ) {
                    Text(stringResource(id = R.string.cancelText))
                }
            }
        )
    }
}



