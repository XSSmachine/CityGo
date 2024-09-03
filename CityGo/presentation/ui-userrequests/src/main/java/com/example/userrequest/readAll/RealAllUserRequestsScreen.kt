package com.example.userrequest.readAll

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.media.CamcorderProfile.getAll
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
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
import androidx.compose.runtime.snapshots.Snapshot.Companion.observe
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.DeviceFontFamilyName
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.userrequest.R
import com.example.userrequest.read.ReadUserRequestViewModel
import com.example.userrequest.read.UserRequestItem
import com.example.userrequest.read.UserRequestListResponseModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.hfad.model.Loading
import com.hfad.model.Triumph
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


private lateinit var context: Context
private lateinit var activity: Activity
private var navController: NavHostController? = null
private lateinit var viewModel: ReadAllUserRequestsViewModel
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ReadAllUserRequestScreen(
    navController: NavController,
    onUserRequestClick: (String, String) -> Unit,
    viewModel: ReadAllUserRequestsViewModel = hiltViewModel(),
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }
    var refreshing by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var hasInternetConnection by remember { mutableStateOf(true) }
    val role = remember { mutableStateOf("") }

    val userRequests = remember { mutableStateListOf<UserRequestListResponseModel>() }

    LaunchedEffect(Unit) {
        hasInternetConnection = checkInternetConnection(context)
        if (hasInternetConnection) {
            viewModel.getAll()
            role.value = viewModel.getUserRole()
        }
    }

    LaunchedEffect(refreshing) {
        if (refreshing) {
            hasInternetConnection = checkInternetConnection(context)
            if (hasInternetConnection) {
                viewModel.getAll()
                role.value = viewModel.getUserRole()
            }
            refreshing = false
        }
    }

    viewModel._requestViewState.observe(lifecycleOwner) { value ->
        when (value) {
            is Loading -> {
                isLoading = true
            }
            is Triumph -> {
                isLoading = false
                when (val data = value.data) {
                    is List<*> -> {
                        userRequests.clear()
                        userRequests.addAll(data.filterIsInstance<UserRequestListResponseModel>())
                    }
                }
            }
            is Error -> {
                isLoading = false
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.assignements)) }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { paddingValues ->
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = refreshing),
            onRefresh = { refreshing = true },
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when {
                    !hasInternetConnection -> {
                        Text(
                            "No internet connection. Please check your network and try again.",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .width(64.dp)
                                .align(Alignment.Center),
                            color = MaterialTheme.colorScheme.secondary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        )
                    }
                    userRequests.isEmpty() -> {
                        Text(
                            "No assignments available.",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            itemsIndexed(userRequests) { index, item ->
                                UserRequestItem(
                                    viewModel,
                                    index = index,
                                    userRequest = item,
                                    onUserRequestClicked = onUserRequestClick
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// Function to check internet connection
fun checkInternetConnection(context: Context): kotlin.Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork
    val capabilities = connectivityManager.getNetworkCapabilities(network)
    return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
}





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserRequestItem(
    viewModel: ReadAllUserRequestsViewModel,
    index: Int,
    userRequest: UserRequestListResponseModel,
    onUserRequestClicked: (String, String) -> Unit
) {

    fun isToday(dateString: String): Boolean {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = formatter.parse(dateString)
        val today = Calendar.getInstance()
        val requestDate = Calendar.getInstance()
        requestDate.time = date ?: return false

        return today.get(Calendar.YEAR) == requestDate.get(Calendar.YEAR) &&
                today.get(Calendar.DAY_OF_YEAR) == requestDate.get(Calendar.DAY_OF_YEAR)
    }

    // Check if the request date is today
    val isRequestToday = isToday(userRequest.timeTable.substring(9))

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
            .padding(vertical = 8.dp, horizontal = 16.dp),
        shape = shape,
        onClick = { onUserRequestClicked(userRequest.userId, userRequest.sid) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Picture with "New" label
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .padding(2.dp)
            ) {
                if (userRequest.photo != null) {
                    Image(
                        painter = rememberAsyncImagePainter(userRequest.photo),
                        contentDescription = "Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(percent = 10))
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = "Image",
                        modifier = Modifier.fillMaxSize()
                    )
                }
                if (isRequestToday) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .background(Color.Yellow, shape = RoundedCornerShape(8.dp))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.new_text),
                            color = Color.DarkGray,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }

            }

            // Texts
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
            ) {
                // Date and Time
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Date",
                        modifier = Modifier.size(16.dp),
                        tint = Color.Green
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = userRequest.timeTable.substring(9),
                        fontWeight = FontWeight.Normal,
                        maxLines = 1,
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 15.sp,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Time
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_access_time_filled_24),
                        contentDescription = "Time",
                        modifier = Modifier.size(16.dp),
                        tint = Color.Cyan
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = userRequest.timeTable.substring(0, 7),
                        fontWeight = FontWeight.Normal,
                        maxLines = 1,
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 15.sp,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Address name
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = userRequest.address1.addressName,
                        modifier = Modifier.padding(vertical = 2.dp),
                        fontFamily = FontFamily(
                            Font(
                                DeviceFontFamilyName("sans-serif"),
                                weight = FontWeight.Light
                            )
                        ),
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))


                    Text(
                        text = userRequest.description,
                        maxLines = 1,
                        fontWeight = FontWeight.W400,
                        color = Color.Black,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 10.sp
                    )


            }
            Spacer(modifier = Modifier.height(4.dp))

            Box(
                modifier = Modifier
                    .background(Color(0xFFFFEB3B), shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = userRequest.price.toString() + " €",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


