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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
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
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@Composable
fun AllDetailScreen(
    ViewModel: DetailUserRequestViewModel,
    userId:String,
    sid:String,
    navigateUp:() -> Unit,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

) {

    context = LocalContext.current
    val focusRequester = remember { FocusRequester() }


    val scope = rememberCoroutineScope()
//    val scaffoldState = rememberScaffoldState()
    val snackBarHostState = remember { SnackbarHostState() }
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    // Get screen width and height for padding calculation
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val screenHeightDp = configuration.screenHeightDp.dp

    viewModel=ViewModel

    activity = ((LocalContext.current as? Activity)!!)

    var offerExists by remember { mutableStateOf<Boolean>(false) }

    val userData = remember { mutableStateOf(
        UserProfileResponseModel(
        id="",
        email="",
        name="",
        surname = "",
        stars=0.0,
        profilePicture ="",
        phoneNumber = "",
        sid = null,
        sync = null,
        requests = null

    )
    )
    }

    val state = remember { mutableStateOf(
        UserRequestResponseModel(
        uuid="",
        date = "",
        userId = "",
        photo = "".toUri(),
        description="",
        address1 = Address("",null,null,true,0,"",""),
        address2= Address("",null,null,true,0,"",""),
        timeTable="",
        category="",
        extraWorker=false,
        price=0,
        sid = "",
        sync = null,
        offers = null
    )
    ) }



    LaunchedEffect(Unit) {
        // Run on first screen compose
        offerExists = viewModel.doesOfferExist(sid)
        viewModel.getRemoteUserRequest(sid)
        viewModel.getProfileDetails(userId)
        viewModel.requestViewState.observe(lifecycleOwner){value ->
            Log.d("DETAIL_STATE",value.toString())
            when(value){

                is Loading ->{

                    Toast.makeText(context,"Loading"+sid, Toast.LENGTH_SHORT).show()
                }
                is Triumph -> {
                    Log.d("WHY", value.data.toString())
                    when(value.data){

                        is UserRequestResponseModel -> {
                            Log.d("WHYY", value.data.toString())
                            state.value = value.data
                        }

                    }
                }
                is Error -> {
                    Log.d("SID", sid)
                    Toast.makeText(context,"No user data", Toast.LENGTH_SHORT).show()
                }
                else -> {

                }
            }
        }

        viewModel.profileViewState.observe(lifecycleOwner){value ->
            when(value){
                is Loading ->{
                    //Loading case
                }
                is Triumph -> {
                    when(value.data){
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

                Box(){
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
                    if (state.value.timeTable.substringAfter(",").equals(currentDate.format(
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
                        Text(text = state.value.timeTable.substringBefore(","))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Button filling whole row



                CreateOfferButton(
                    detailUserRequestViewModel = viewModel,
                    navigateUp = { navigateUp },
                    requestId = sid,
                    offerExists,
                    state.value
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
                    Text(text = state.value.price.toString()+" €")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Separator with description text
                Divider()
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Description")
//                Text(text = detailUserRequestViewModel.state?.description.toString())
                OutlinedTextField(
                    value = state.value.description,
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
                    if (state.value.extraWorker){
                        Text(text = "Need 2 workers for the job")
                    }else{
                        Text(text = "Only 1 worker for the job")
                    }
                    Spacer(modifier = Modifier.height(6.dp))

                    Card {
                        if (state.value.extraWorker){


                            Row {
                                Icon(painter = painterResource(id = R.drawable.baseline_directions_run_24), contentDescription = "Worker")
                                Icon(painter = painterResource(id = R.drawable.baseline_directions_run_24), contentDescription = "Worker")
                            }
                        }else{
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
                state.value.address1.let { it1 ->
                    DisplayVerticalText(
                        text1 = it1.addressName,
                        text2 = it1.liftStairs,
                        text3 = it1.floor.toString(),
                        text4 = it1.doorCode.toString(),
                        text5 = it1.phoneNumber,
                        icon = R.drawable.baseline_rocket_launch_24,
                        desc = "Pickup address"
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
                        )
                    )

                }
                Spacer(modifier = Modifier.height(12.dp))
                Row {

                    if (userData.value.profilePicture.toString().isNullOrEmpty()){
                        Image(painter = painterResource(id = R.drawable.user), contentDescription = "Default user image", modifier = Modifier.size(100.dp)
                        )
                    }else{
                        AsyncImage(model = userData.value.profilePicture?.toUri(), contentDescription = "Owner image")
                    }
                    Spacer(modifier = Modifier.size(4.dp))

                    Column {
                        Text(text = userData.value.name)
                        Spacer(modifier = Modifier.size(4.dp))
                        Row {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Review rating",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = userData.value.stars.toString())
                        }

                    }

                }

            }
        }
    )

}