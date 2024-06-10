package com.example.userrequest.readAll

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.media.CamcorderProfile.getAll
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.Snapshot.Companion.observe
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
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
import com.example.userrequest.R
import com.example.userrequest.read.ReadUserRequestViewModel
import com.example.userrequest.read.UserRequestItem
import com.example.userrequest.read.UserRequestListResponseModel
import com.hfad.model.Address
import com.hfad.model.Loading
import com.hfad.model.Triumph


private lateinit var context: Context
private lateinit var activity: Activity
private var navController: NavHostController? = null
private lateinit var viewModel: ReadAllUserRequestsViewModel
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ReadAllUserRequestScreen(
    navController: NavController,
    onUserRequestClick: (String,String) -> Unit,
    ViewModel: ReadAllUserRequestsViewModel = hiltViewModel(),
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



    LaunchedEffect(Unit) {
        // Run on first screen compose
        viewModel.getAll()
        role.value = viewModel.getUserRole()
        viewModel._requestViewState.observe(lifecycleOwner){ value ->
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
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Assignements")
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
                itemsIndexed(userRequests) { index, item ->

                    UserRequestItem(viewModel,index = index, userRequest = item, onUserRequestClicked = onUserRequestClick)

                }
            }


        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserRequestItem(
    viewModel: ReadAllUserRequestsViewModel,
    index: Int,
    userRequest: UserRequestListResponseModel,
    onUserRequestClicked: (String,String) -> Unit
) {
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
        onClick = { onUserRequestClicked(userRequest.userId,userRequest.sid) }
    ) {
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
                        fontWeight = FontWeight.Normal,
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

                // Button
                AnimatedVisibility(visible = viewModel.getUserRole()=="Cygo") {
                    Button(
                        onClick = { /* Navigate to create Offer */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Javi dostupnost")
                    }
                }

            }
        }
    }
}

