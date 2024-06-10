package com.example.userrequest.read

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.userrequest.R
import com.example.userrequest.readAll.UserRequestItem
import com.hfad.model.Address
import com.hfad.model.ErrorCode
import com.hfad.model.Loading
import com.hfad.model.OfferResponseModel
import com.hfad.model.Triumph
import kotlinx.coroutines.runBlocking

private lateinit var context: Context
private lateinit var activity: Activity
private var navController: NavHostController? = null
private lateinit var viewModel: ReadUserRequestViewModel
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ReadUserRequestScreen(
    navController: NavController,
    onUserRequestClick: (String,String) -> Unit,
    onUserRequestButtonClick: (String) -> Unit,
    onCygoOfferClick: (String,String) -> Unit,
    ViewModel: ReadUserRequestViewModel = hiltViewModel(),
    lifecycleOwner:LifecycleOwner = LocalLifecycleOwner.current
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

    viewModel = ViewModel

    activity = ((LocalContext.current as? Activity)!!)


    val role = remember { mutableStateOf("") }

    val userRequests = remember { mutableStateListOf(
        UserRequestListResponseModel(
            uuid = "",
            userId="",
            photo="".toUri(),
            description="",
            address1 = Address("123 Main St", null, null, false, 0, "", ""),
            address2 = Address("123 Main St", null, null, false, 0, "", ""),
            timeTable="",
            category="",
            extraWorker=false,
            price=0,
            sid = ""

        )) }


    val SPuserRequests =

        remember { mutableStateListOf(
        UserRequestListResponseModel(
            uuid = "",
            userId="",
            photo="".toUri(),
            description="",
            address1 = Address("123 Main St", null, null, false, 0, "", ""),
            address2 = Address("123 Main St", null, null, false, 0, "", ""),
            timeTable="",
            category="",
            extraWorker=false,
            price=0,
            sid = ""
        )) }



    LaunchedEffect(Unit) {
        // Run on first screen compose
        role.value = viewModel.getUserRole()
        if(viewModel.getUserRole()=="Cygo"){
            viewModel.getServiceProviderOffers()
        }else{
            viewModel.getMyUserRequest()
        }
        viewModel.userRequests.observe(lifecycleOwner){ value ->
            when(value){
                is Loading ->{
                    //Loading case
                }
                is Triumph -> {
                    when(value.data){
                        is List<UserRequestListResponseModel> -> {
                            userRequests.clear()
                            userRequests.addAll(value.data)

                        }

                    }
                }
                is Error -> {


                }
                else -> {}
            }
        }

        viewModel.spUserRequests.observe(lifecycleOwner){ value ->
            when(value){
                is Loading ->{
                    //Loading case
                }
                is Triumph -> {
                    when(value.data){
                        is List<UserRequestListResponseModel> -> {
                            SPuserRequests.clear()
                            SPuserRequests.addAll(value.data)
                        }

                    }
                }
                is Error -> {

                }
                else -> {}
            }
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "My activities")
                },

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
                if(role.value=="Owner"){
                    itemsIndexed(userRequests) { index, item ->

                        UserRequestItem(index = index, userRequest = item, onUserRequestClicked = onUserRequestClick,onUserRequestButtonClicked = onUserRequestButtonClick,onCygoOfferClick=onCygoOfferClick,
                            viewModel,lifecycleOwner)

                    }
                }else{
                    itemsIndexed(SPuserRequests) { index, item ->

                        UserRequestItem(index = index, userRequest = item, onUserRequestClicked = onUserRequestClick, onUserRequestButtonClicked = onUserRequestButtonClick,onCygoOfferClick=onCygoOfferClick,
                            viewModel,lifecycleOwner)

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
    onUserRequestClicked: (String,String) -> Unit,
    onUserRequestButtonClicked: (String) -> Unit,
    onCygoOfferClick: (String,String) -> Unit,
    viewModel: ReadUserRequestViewModel,
    lifecycleOwner:LifecycleOwner
) {

    val isLoading = remember { mutableStateOf(true) }
    val status = remember { mutableStateOf("") }
    val trueFalse = remember { mutableStateOf(false) }
    val offer = remember { mutableStateOf(
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
    ) }

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

    LaunchedEffect(Unit) {
        status.value = viewModel.getSingleOfferStatus(userRequest.uuid)
        viewModel.getSingleOffer(userRequest.sid)
        if(!userRequest.sid.isNullOrEmpty()){
            Log.d("TRUE/FALSE",viewModel.checkOffersForRequest(userRequest.sid).toString())
            trueFalse.value = viewModel.checkOffersForRequest(userRequest.sid)
        }



        viewModel.singleOffer.observe(lifecycleOwner) { value ->
            when (value) {
                is Loading -> {
                    //Loading case
                }

                is Triumph -> {
                    when (value.data) {
                        is OfferResponseModel -> {
                            Log.d("userOffer-Sucess", value.data.toString())
                            offer.value = value.data

                        }

                    }
                }

                is Error -> {
                    Log.d("userOffer-Error", value.toString())

                }

                else -> {}
            }
        }
        isLoading.value = false
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp),
        shape = shape,
        onClick = {
            if(viewModel.getUserRole()=="Owner")
            onUserRequestClicked(userRequest.uuid,userRequest.userId)
            else
                onCygoOfferClick(userRequest.userId,userRequest.sid)
        }
    ) {
        if (viewModel.getUserRole()=="Cygo"){
            Row {
                Text(text = "Your offer is: "+status.value)
                Spacer(modifier = Modifier.width(12.dp))
                Button(onClick = {
                    runBlocking{
                        viewModel.deleteOffer(offer.value.sid!!)
                    } }) {

                }
            }
        }
        
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Picture
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .padding(8.dp)
            ) {
                if (userRequest.photo != null) {
                    Image(
                        painter = rememberAsyncImagePainter(userRequest.photo),
                        contentDescription = "Image",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(shape = RoundedCornerShape(8.dp))
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = "Image",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            // Texts
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
            ) {
                // Timetable
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Timetable",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = userRequest.timeTable.substringAfter(","),
                        maxLines = 1,
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 15.sp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Address name
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Address",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${userRequest.address1.addressName} | ${userRequest.address2.addressName}",
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 10.sp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Price
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Price",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Ukupna cijena ${userRequest.price}",
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                if (isLoading.value){
                    CircularProgressIndicator(
                        modifier = Modifier.width(34.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                }else {
                    if (trueFalse.value && viewModel.getUserRole() == "Owner") {
                        Button(
                            onClick = { onUserRequestButtonClicked(userRequest.sid) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Yellow,
                                contentColor = Color.Black
                            )
                        ) {
                            Text(text = "Select a cygo")
                        }
                    } else if (viewModel.getUserRole() == "Owner") {
                        Button(
                            onClick = { /* update offer price by 5 euro */ },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Yellow,
                                contentColor = Color.Black
                            )
                        ) {
                            Text(text = "+5€")
                        }
                    }
                }
                // Button

            }
        }
    }
}



