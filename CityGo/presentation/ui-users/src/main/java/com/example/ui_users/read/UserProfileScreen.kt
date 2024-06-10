@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.ui_users.read

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.net.Uri
import android.os.Build
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.ui_users.R
import com.hfad.model.Address
import com.hfad.model.Loading
import com.hfad.model.Triumph
import com.hfad.model.UserProfileResponseModel
import com.hfad.model.UserRequestResponseModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

private lateinit var context: Context
private lateinit var activity: Activity
private var navController: NavHostController? = null
private lateinit var viewModel: UserProfileViewModel
@RequiresApi(Build.VERSION_CODES.N)
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
fun UserProfileScreen(
    navController: NavController,
    ViewModel: UserProfileViewModel = hiltViewModel(),
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

    viewModel= ViewModel

    activity = ((LocalContext.current as? Activity)!!)

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
    )}



    LaunchedEffect(Unit) {
        // Run on first screen compose

        viewModel.viewState.observe(lifecycleOwner) { value ->
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
    val pagerState = rememberPagerState(pageCount = {2})
    val selectedTabIndex = remember{ derivedStateOf { pagerState.currentPage }}
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
            ,horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top

        )
        {
            TabRow(
                selectedTabIndex = selectedTabIndex.value,
                modifier = Modifier.fillMaxWidth(),
//                containerColor = Color.Yellow,
                divider= {}, indicator = { tabPositions ->
                    if (selectedTabIndex.value < tabPositions.size) {
                        TabRowDefaults.Indicator(
                            modifier = Modifier
                                .tabIndicatorOffset(tabPositions[selectedTabIndex.value]),
                            color = Color.Black,
                        )
                    }
                },


            ) {
                Tab(
                    text = { Text("Owner") },
                    selected = selectedTabIndex.value == 0,
                    onClick = { scope.launch { pagerState.animateScrollToPage(0) }
                         }
                )
                Tab(
                    text = { Text("CyGo") },
                    selected = selectedTabIndex.value == 1,
                    onClick = { scope.launch { pagerState.animateScrollToPage(1)} }
                )
            }
        TabContent(pagerState = pagerState, userViewModel = viewModel, navController = navController,userData.value)
        }
    }

    }

@Composable
private fun CreateInfo(userViewModel: UserProfileResponseModel,viewModel: UserProfileViewModel,navController: NavController) {
    Column(
        modifier = Modifier
            .padding(2.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "⭐ "+userViewModel.stars.toString(),
            modifier = Modifier.padding(3.dp),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(5.dp))
        Row {
            Text(
                style = MaterialTheme.typography.headlineMedium,
                text = userViewModel.name
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = userViewModel.surname,
                style = MaterialTheme.typography.headlineMedium,
            )
        }


        Text(
            color = Color.Black,
            text = if(userViewModel.email.toString().isNullOrEmpty()) "-----" else userViewModel.email.toString(),
            modifier = Modifier.padding(3.dp),
            style = MaterialTheme.typography.bodyMedium
        )


        Spacer(modifier = Modifier.padding(5.dp))
        Button(onClick = {
            runBlocking { viewModel.clearUserId() }
            navController.navigate("login")
            

        },contentPadding = PaddingValues(5.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black
            )
            
        ) {

            Text(text = "Log out")
        }

        Button(onClick = {
            viewModel.updateServiceProviderStatus("Accepted")
//            navController.navigate("provider")


        },contentPadding = PaddingValues(5.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black
            )

        ) {

            Text(text = "Accept Application")
        }
    }
}

@Composable
fun MotivationCard(title: String, body: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp)
    ) {
        Box(
        ) {
            Column(
                modifier = Modifier
                    .padding(6.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = title,
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(22.dp)
                )
                Text(
                    text = body,
                    style = TextStyle(fontWeight = FontWeight.Normal)
                )
            }
            
        }
    }
}

@Composable
fun AdCard(title: String, body: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp)
    ) {
        Box(
        ) {
            Column(
                modifier = Modifier
                    .padding(6.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = title,
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = body,
                    style = TextStyle(fontWeight = FontWeight.Normal)
                )
            }
            Image(
                painter = painterResource(id = R.drawable.arrow_right_long),
                contentDescription = "Arrow",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            )
        }
    }
}




@Composable
private fun CreateImageProfile(userViewModel: UserProfileResponseModel,modifier: Modifier = Modifier) {
    Surface(
        modifier = Modifier
            .size(124.dp),
        shape = CircleShape,
        border = BorderStroke(2.dp, Color.LightGray),
        tonalElevation = 4.dp,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
    ) {
        if (userViewModel.profilePicture.toString().isNotEmpty()) {
            AsyncImage(
                model = userViewModel.profilePicture.toString().toUri(),
                contentDescription = "user image",
                contentScale = ContentScale.Crop,            // crop the image if it's not a square
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.DarkGray, CircleShape)// clip to the circle shape
            )
        } else {
            Image(
                painter = painterResource(R.drawable.user),
                contentDescription = "avatar",
                contentScale = ContentScale.Crop,            // crop the image if it's not a square
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.DarkGray, CircleShape)// clip to the circle shape
            )
        }

    }
}

@Composable
private fun ProfileScreen(userData:UserProfileResponseModel,userViewModel: UserProfileViewModel, navController: NavController) {
    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .height(420.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                IconButton(
                    onClick = { navController.navigate("updateDialog") },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Create,
                        contentDescription = null // Decorative element
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    CreateImageProfile(userData)
                    CreateInfo(userData,userViewModel, navController)
                }
            }
        }

        AdCard(title = "Help us, invite your friends", body = "Refer to your friends and gift them 5 € discount when they create their first order, we will donate 5 € to Plant-for-the-Planet.")
        ClickableIconTextRow(icon = painterResource(id = R.drawable.pin_alt_fill), text = "Saved address:" ) {

        }
        ClickableIconTextRow(icon = painterResource(id = R.drawable.bell_fill), text = "Notifications" ) {

        }
        ClickableIconTextRow(icon = painterResource(id = R.drawable.chat_fill), text = "Support" ) {

        }
        ClickableIconTextRow(icon = painterResource(id = R.drawable.book_check_fill), text = "Become a courier" ) {

        }
        
        MotivationCard(title = "We simply want better planet and more efficient services", body = "")

        Row {

            Spacer(modifier = Modifier.width(20.dp))
                Text(text = "Logout",style = TextStyle(fontWeight = FontWeight.Light), modifier = Modifier.clickable{
                    runBlocking { userViewModel.clearUserId() }
                    navController.navigate("login")
                })

            Spacer(modifier = Modifier.width(105.dp))
            Text(text = "Version: 1.0.0",style = TextStyle(fontWeight = FontWeight.Light))
        }
        Spacer(modifier = Modifier.size(50.dp))
    }
}

@RequiresApi(Build.VERSION_CODES.N)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabContent(pagerState: PagerState,userViewModel: UserProfileViewModel,navController: NavController,userData:UserProfileResponseModel) {
    HorizontalPager(state = pagerState) { index ->
        when (index) {
            0 -> {
                // Content for the first tab (e.g., HomeScreen)
                ProfileScreen( userData = userData, userViewModel = userViewModel, navController = navController)
                userViewModel.setUserRole("Owner")
            }

            1 -> {
                // Content for the second tab (e.g., SearchScreen)
                if (userViewModel.statusLiveData=="Accepted"){
                    ServiceProviderProfileScreen(navController,userViewModel)
                    userViewModel.setUserRole("Cygo")
                }else{
                    PendingScreen(navController = navController,userViewModel)

                }
                
            }

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageInput(text: String, onImageUriReceived: (Uri?) -> Unit) {
    Column {
        val sheetState = rememberModalBottomSheetState()
        var isSheetOpen by rememberSaveable {
            mutableStateOf(false)
        }
        var imageUri by remember {
            mutableStateOf<Uri?>(null)
        }
        if (imageUri != null) {
            AsyncImage(
                model = imageUri,
                contentDescription = "Image",
                modifier = Modifier
                    .size(50.dp)
                    .clickable(onClick = { isSheetOpen = true })
            )
        }

        else if (text=="User"){

            Image(
                painter = painterResource(R.drawable.user),
                contentDescription = "avatar",
                contentScale = ContentScale.Crop,            // crop the image if it's not a square
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.DarkGray, CircleShape)
                    .clickable(onClick = { isSheetOpen = true })

            )
        }else if (text=="Selfie" || text=="Vehicle"){

            Image(
                painter = painterResource(R.drawable.group_selfie),
                contentDescription = "icon",
                // crop the image if it's not a square
                modifier = Modifier
                    .clickable(onClick = { isSheetOpen = true })

            )
        }
        else if (text=="ID Card Front" || text=="ID Card Back"){

            Image(
                painter = painterResource(R.drawable.group_id),
                contentDescription = "icon",
                modifier = Modifier
                    .clickable(onClick = { isSheetOpen = true })

            )
        }

        else {
            Button(
                onClick = {
                    isSheetOpen = true

                    // Open the bottom dialog and pass the callback function

                }
            ) {
                Text(text)
            }
        }

        if (isSheetOpen) {

            ModalBottomSheet(
                onDismissRequest = { isSheetOpen=false },
                sheetState = sheetState,
                shape = RoundedCornerShape(25.dp),
                containerColor = Color.Gray,
                content = {
                    Column(
                        content = {
//                               Spacer(modifier = Modifier.padding(16.dp))
                            Row(modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center) {
                                if (text!="Selfie"){
                                    SinglePhotoPicker { uri ->
                                        if (uri != null) {
                                            imageUri=uri
                                            onImageUriReceived(uri)
                                        }
                                        isSheetOpen=false
                                    }
                                    Spacer(modifier = Modifier.width(30.dp))
                                }

                                CameraPhotoTaker { uri ->
                                    if (uri != null) {
                                        imageUri=uri
                                        onImageUriReceived(uri)
                                    }
                                    isSheetOpen=false
                                }
                            }
                        }
                    )
                },
                modifier =
                Modifier
                    .fillMaxWidth()
                    .height(200.dp)

            )
        }
    }
}




@Composable
fun SinglePhotoPicker(onPhotoPicked: (Uri?) -> Unit){
    val context = LocalContext.current
    val file = remember { context.createImageFile() }
    val uri = remember { FileProvider.getUriForFile(context, "${context.packageName}.provider", file) }
    val singlePhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { pickedImageUri: Uri? ->
            if (pickedImageUri != null) {
                var inputStream: InputStream? = null
                var outputStream: OutputStream? = null
                try {
                    inputStream = context.contentResolver.openInputStream(pickedImageUri)
                    outputStream = context.contentResolver.openOutputStream(uri)
                    if (inputStream != null && outputStream != null) {
                        inputStream.copyTo(outputStream)
                        onPhotoPicked(uri)
                    }
                } catch (e: IOException) {
                    // Handle the error
                } finally {
                    inputStream?.close()
                    outputStream?.close()
                }
            }
        }
    )

    Column {
        Button(
            onClick = {singlePhotoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
            shape = RoundedCornerShape(10.dp), // This makes the button square
            colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow)) {
            Image(painterResource(R.drawable.baseline_image_24), "gallery", modifier = Modifier.size(100.dp) )
        }

//        AsyncImage(
//            model = uri,
//            contentDescription = null,
//            modifier = Modifier.size(250.dp)
//            )
    }
}

@Composable
fun CameraPhotoTaker(onPhotoTaken: (Uri?) -> Unit) {
    val context = LocalContext.current
    val file = remember { context.createImageFile() }
    val uri = remember { FileProvider.getUriForFile(context, "${context.packageName}.provider", file) }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            onPhotoTaken(uri)
        } else {
            Toast.makeText(context, "Failed to capture image", Toast.LENGTH_SHORT).show()
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            Toast.makeText(context, "Permission granted!", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permission denied!", Toast.LENGTH_SHORT).show()
        }
    }

    ImageTaker(
        cameraLauncher = cameraLauncher,
        context = context,
        uri = uri,
        permissionLauncher = permissionLauncher,
        onPhotoTaken = onPhotoTaken // Pass the callback to ImageTaker
    )
}

@Composable
fun ImageTaker(
    cameraLauncher: ManagedActivityResultLauncher<Uri, Boolean>,
    context: Context,
    uri: Uri,
    permissionLauncher: ManagedActivityResultLauncher<String, Boolean>,
    onPhotoTaken: (Uri) -> Unit // Callback to pass the URI to the caller
) {
    Column {
        Button(
            onClick = {
                val permissionCheckResult = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                    cameraLauncher.launch(uri)
                } else {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
            },
            shape = RoundedCornerShape(10.dp), // This makes the button square
            colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow)
        ) {
            Image(painterResource(R.drawable.baseline_camera_alt_24), "gallery", modifier = Modifier.size(100.dp) )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun ShowDatePicker(context: Context, onDateSelected: (String) -> Unit) {
    val calendar = remember { Calendar.getInstance() }
    val selectedDate = remember { mutableStateOf("") }

    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, day: Int ->
                selectedDate.value = "$day/$month/$year"
                onDateSelected(selectedDate.value) // Invoke the callback with the selected date
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        TextField(
            value = selectedDate.value,
            onValueChange = { selectedDate.value=it },
            label = { Text("Date of birth", color = Color.Black) },
            enabled=false,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { datePickerDialog.show() },
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.DarkGray,
                disabledTextColor = Color.Transparent,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Gray,
                unfocusedIndicatorColor = Color.LightGray,
                disabledIndicatorColor = Color.LightGray,
                disabledContainerColor = Color.White,
                disabledPlaceholderColor = Color.Black)
        )

    }
}

@Composable
fun ClickableIconTextRow(icon: Painter, text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(painter = icon, contentDescription = null)
        Spacer(Modifier.width(8.dp))
        Text(text = text)
    }
}


