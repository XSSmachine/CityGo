package com.example.userrequest.details

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.fonts.FontStyle
import android.os.Build
import android.support.v4.os.IResultReceiver2.Default
import android.util.Log
import android.widget.CheckBox
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter.State.Empty.painter
import coil.compose.rememberAsyncImagePainter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.userrequest.R


import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailScreen(
    detailUserRequestViewModel: DetailUserRequestViewModel,
    requestId:Long,
    userId:String,
    navigateUp:() ->Unit,
    onUserRequestUpdate: (Int,String) -> Unit

) {



    LaunchedEffect(key1 = Unit) {

            detailUserRequestViewModel.getUserRequest(requestId.toInt(),userId)
            detailUserRequestViewModel.getProfileDetails(userId)

    }

    Box(

        content = {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {

            Box(){
                Image(
                    painter = rememberAsyncImagePainter(detailUserRequestViewModel.state?.photo),
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
                    if (detailUserRequestViewModel.getIdValue()==userId){
                        Button(onClick = { onUserRequestUpdate(requestId.toInt(),userId) },
                            colors= ButtonDefaults.buttonColors(
                                containerColor = Color.Yellow,
                                contentColor = Color.Black
                            )) {
                            Icon(Icons.Default.Create, contentDescription = "Update")
                        }
                        }

                    Button(onClick = { /* Handle second button click */ },
                        colors= ButtonDefaults.buttonColors(
                            containerColor = Color.Yellow,
                        contentColor = Color.Black
                    )) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Column with pickup time
                Row {
                    Image(imageVector = Icons.Default.DateRange, contentDescription = "Pick-up time")
                    Text(text = "Pick-up Time")
                }
                Column {
                    val currentDateTime = LocalDateTime.now(ZoneId.of("CET"))

                    // Get current date and next day
                    val currentDate = currentDateTime.toLocalDate()
                    if (detailUserRequestViewModel.state.timeTable.substringAfter(",").equals(currentDate.format(
                            DateTimeFormatter.ofPattern("dd/MM/yyyy")))){
                        Text(text = "Today")
                    }else{
                        Text(text = "Tommorow")
                    }

                    FilledIconToggleButton(
                        checked = false,
                        onCheckedChange = { //nothing
                        },
                        modifier = Modifier
                            .padding(vertical = 3.dp, horizontal = 13.dp)
                            .size(65.dp, 35.dp)
                    ) {
                        Text(text = detailUserRequestViewModel.state.timeTable.substringBefore(","))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Button filling whole row

                if (detailUserRequestViewModel.getIdValue()==userId){
                    Button(
                        onClick = { detailUserRequestViewModel.deleteUserRequest(requestId.toInt(),userId)
                            navigateUp()},
                        modifier = Modifier.fillMaxWidth(),
                        colors= ButtonDefaults.buttonColors(
                            containerColor = Color.Yellow,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(text = "Delete User Request")
                    }
                }


                CreateOfferButton(
                    detailUserRequestViewModel = detailUserRequestViewModel,
                    navigateUp = { navigateUp },
                    requestId = requestId,
                    detailUserRequestViewModel.doesOfferExist(requestId.toInt())
                )


                Spacer(modifier = Modifier.height(16.dp))

                // Separator
                Divider()

                Spacer(modifier = Modifier.height(16.dp))

                // Row to display price
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Price")
                    Text(text = detailUserRequestViewModel.state?.price.toString()+" €")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Separator with description text
                Divider()
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Description")
//                Text(text = detailUserRequestViewModel.state?.description.toString())
                OutlinedTextField(
                    value = detailUserRequestViewModel.state?.description.toString(),
                    onValueChange = { /* Do nothing */  },
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
                    if (detailUserRequestViewModel.state.extraWorker){
                        Text(text = "Need 2 workers for the job")
                    }else{
                        Text(text = "Only 1 worker for the job")
                    }
                    Spacer(modifier = Modifier.height(6.dp))

                    Card {
                        if (detailUserRequestViewModel.state.extraWorker){


                        Row {
                            Icon(painter = painterResource(id = R.drawable.baseline_directions_run_24), contentDescription = "Worker")
                            Icon(painter = painterResource(id = R.drawable.baseline_directions_run_24), contentDescription = "Worker")
                        }}else{
                            Row {
                                Icon(painter = painterResource(id = R.drawable.baseline_directions_run_24), contentDescription = "Worker")
                            }
                        }
                    }

                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Google Map Window")

                Spacer(modifier = Modifier.height(16.dp))

                // Custom composable for displaying 5 strings vertically
                detailUserRequestViewModel.state?.address1?.let { it1 ->
                    DisplayVerticalText(
                        text1 = it1.addressName,
                        text2 = detailUserRequestViewModel.state!!.address1.liftStairs,
                        text3 = detailUserRequestViewModel.state!!.address1.floor.toString(),
                        text4 = detailUserRequestViewModel.state!!.address1!!.doorCode.toString(),
                        text5 = detailUserRequestViewModel.state!!.address1.phoneNumber,
                        icon = R.drawable.baseline_rocket_launch_24,
                        desc = "Pickup address"
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Divider()

                detailUserRequestViewModel.state?.address2?.let { it1 ->
                    DisplayVerticalText(
                        text1 = it1.addressName,
                        text2 = detailUserRequestViewModel.state!!.address2.liftStairs,
                        text3 = detailUserRequestViewModel.state!!.address2.floor.toString(),
                        text4 = detailUserRequestViewModel.state!!.address2!!.doorCode.toString(),
                        text5 = detailUserRequestViewModel.state!!.address2.phoneNumber,
                        icon = R.drawable.baseline_done_all_24,
                        desc = "Delivery address"
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Divider()

                Row {
                    Text(
                        text = "Owner",
                        style = TextStyle(
                            fontWeight = FontWeight.Light, // Set the font weight to bold
                            fontSize = 20.sp // Set the font size to 20sp (adjust as needed)
                        ))

                }
                Spacer(modifier = Modifier.height(12.dp))
                Row {

                    if (detailUserRequestViewModel.state.photo.toString().isNullOrEmpty()){
                        Image(painter = painterResource(id = R.drawable.user), contentDescription = "Default user image", modifier = Modifier.size(100.dp)
                            )
                    }else{
                        Image(painter = painterResource(id = R.drawable.user), contentDescription = "Default user image", modifier = Modifier.size(100.dp))

                    }
                    Spacer(modifier = Modifier.size(4.dp))

                    Column {
                        Text(text = detailUserRequestViewModel.data.name)
                        Spacer(modifier = Modifier.size(4.dp))
                        Row {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Review rating",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = detailUserRequestViewModel.data.stars.toString())
                        }

                    }

                }

            }
        }
    )

}

@Composable
fun CreateOfferButton(
    detailUserRequestViewModel: DetailUserRequestViewModel,
    navigateUp: () -> Unit,
    requestId: Long,
    offerExist:Boolean
) {
    var dialogOpen by remember { mutableStateOf(false) }

    if (detailUserRequestViewModel.getUserRole() == "Cygo"){
        Log.d("REQUESTID-",requestId.toString())
        if (offerExist) {

            Button(
                onClick = { dialogOpen = false },
                modifier = Modifier.fillMaxWidth(),
                enabled = false
            ) {
                Text(text = "Already sent application")
            }
        }else {
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
            requestId = requestId
        )
    }
}



@Composable
fun DisplayVerticalText(text1: String, text2: Boolean, text3: String, text4: String, text5: String,icon:Int,desc:String) {
    Column {
        Row {
            Icon(painter = painterResource(id = icon), contentDescription = "address")
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = desc)
        }
        Spacer(modifier = Modifier.size(12.dp))
        Text(text = text1)
        Spacer(modifier = Modifier.size(7.dp))
        if(text2){
            Text(text = "Lift")
        }else{
            Text(text = "Stairs")
        }

        Spacer(modifier = Modifier.size(7.dp))
        Text(text = "Floors: "+text3)
        Spacer(modifier = Modifier.size(7.dp))
        if (text4.isNullOrEmpty()){
            Text(text = "Door code (optional): N/A")
        }else{
            Text(text = "Door code (optional): "+ text4)
        }

        Spacer(modifier = Modifier.size(7.dp))
        Row {
            Text(
                text = "Contact: ",
                style = TextStyle(
                    fontWeight = FontWeight.ExtraBold, // Set the font weight to bold
                    fontSize = 20.sp // Set the font size to 20sp (adjust as needed)
                ))
            Text(
                text = text5,
                style = TextStyle(
                    fontWeight = FontWeight.Bold, // Set the font weight to bold
                    fontSize = 20.sp, // Set the font size to 24sp (adjust as needed)
                    color = Color.Blue // Set the text color to blue
                )
            )
        }
    }
}


@Composable
fun CreateOfferDialogScreen(
    viewModel:DetailUserRequestViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
    requestId: Long,


) {
    var confirmEnabled by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(true) } // Add this line to control the visibility of the dialog

    if (showDialog) { // Wrap your AlertDialog inside this condition
        AlertDialog(
            onDismissRequest = {
                showDialog = false
            }, // The dialog will be dismissed when clicking outside
            title = { Text("Apply for the job") },
            text = {
                Column {
                    TextField(
                        value = viewModel.price,
                        onValueChange = {
                            viewModel.setPrice(it)
                            confirmEnabled =
                                viewModel.price.isNotBlank()
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        label = { Text("Price") }
                    )
                    TextField(
                        value = viewModel.timeTable,
                        onValueChange = {
                            viewModel.setTimeTable(it)
                            confirmEnabled = viewModel.price.toString()
                                .isNotBlank() && viewModel.timeTable.isNotBlank()
                        },
                        label = { Text("Timetable(leave empty if current is ok)") }
                    )
                    CheckBox(confirmEnabled){
                        confirmEnabled=it
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        runBlocking {
                            viewModel.createOffer(
                                requestId = requestId,
                                viewModel.price.toInt(),
                                viewModel.timeTable
                            )
                            navigateUp()
                        }
                        showDialog =
                            false // The dialog will be dismissed when the confirm button is clicked
                    },
                    enabled = confirmEnabled
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = null // Set dismissButton to null to make the dialog not dismissable on click outside
        )
    }
}

@Composable
fun CheckBox(checkedState: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row {
        Checkbox(
            checked = checkedState,
            onCheckedChange = onCheckedChange
        )
        Text(text = "I agree to do these job on these terms.")
    }
}

