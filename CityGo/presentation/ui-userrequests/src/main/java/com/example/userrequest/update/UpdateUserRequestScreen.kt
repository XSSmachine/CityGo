package com.example.userrequest.update

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.userrequest.R
import com.example.userrequest.create.createImageFile
import com.hfad.model.Address
import com.hfad.model.Loading
import com.hfad.model.Triumph
import com.hfad.model.UserRequestResponseModel
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

private lateinit var context: Context
private lateinit var activity: Activity
private var navController: NavHostController? = null
private lateinit var viewModel: UpdateUserRequestViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateUserRequestScreen(
    ViewModel: UpdateUserRequestViewModel,
    requestId: String,
    userId: String,
    navigateUp: () -> Unit,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    context = LocalContext.current
    viewModel = ViewModel
    activity = ((LocalContext.current as? Activity)!!)

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
                sid = null,
                sync = null,
                offers = null
            )
        )
    }



    LaunchedEffect(Unit) {
        viewModel.getUserRequest(requestId, userId)
        viewModel.viewState.observe(lifecycleOwner) { value ->
            when (value) {
                is Loading -> {}

                is Triumph -> {
                    when (value.data) {
                        is UserRequestResponseModel -> {
                            state.value = value.data
                        }

                    }
                }

                is Error -> {}
                else -> {}
            }
        }
    }


    LaunchedEffect(Unit) {
        viewModel.getUserRequest(requestId, userId)
    }
    val saveButtonEnabled =
        state.value.description.isNotBlank() && state.value.timeTable.isNotBlank()
    val saveButtonAction: suspend () -> Unit = {
        viewModel.updateUserRequest(requestId, userId, state.value)
        navigateUp()
    }



    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Update User Request") },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Button(onClick = {
                        viewModel.viewModelScope.launch {
                            saveButtonAction()
                        }
                    }) {
                        Text("Save")
                    }

                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(top = 50.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                ImageInput(state.value.photo.toString()) { uri ->
                    if (uri != null) {
                        state.value = state.value.copy(photo = uri)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = state.value.description,
                    onValueChange = { newValue ->
                        state.value = state.value.copy(description = newValue)
                    },
                    label = { Text(text = "Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 8.dp
                    )
                ) {
                    Text(text = state.value.category)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 8.dp
                    )
                ) {
                    ExtraWorkerSwitch(
                        isChecked = state.value.extraWorker,
                        onCheckedChange = { isChecked ->
                            state.value = state.value.copy(extraWorker = isChecked)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Address 1 TextFields
                addressDetailss1(state.value) {
                    state.value = it
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Address 2 TextFields
                addressDetailss2(state.value) {
                    state.value = it
                }
                Spacer(modifier = Modifier.height(32.dp))
                TextField(
                    value = state.value.price.toString(),
                    onValueChange = { state.value = state.value.copy(price = it.toInt()) },
                    label = { Text(text = "Price") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}

@Composable
fun ExtraWorkerSwitch(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Switch(
        checked = isChecked,
        onCheckedChange = onCheckedChange,
        colors = SwitchDefaults.colors( // Customize switch colors if needed
            checkedThumbColor = MaterialTheme.colorScheme.primary,
            uncheckedThumbColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
            uncheckedTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
    )
}


@Composable
fun addressDetailss1(
    state: UserRequestResponseModel,
    onStateChange: (UserRequestResponseModel) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.width(16.dp))
        Text("Početna adresa")
        Spacer(modifier = Modifier.width(16.dp))
        OutlinedTextField(
            value = state.address1.addressName,
            onValueChange = { value ->
                onStateChange(state.copy(address1 = state.address1.copy(addressName = value)))
            },
            label = { Text("Traži adresu") }
        )
        Spacer(modifier = Modifier.width(16.dp))
        Row {
            val pattern = remember { Regex("^\\d*\$") }
            OutlinedTextField(
                modifier = Modifier
                    .width(90.dp)
                    .height(60.dp)
                    .padding(start = 15.dp, top = 10.dp, end = 15.dp)
                    .background(Color.White, RoundedCornerShape(5.dp)),
                shape = RoundedCornerShape(5.dp),
                value = state.address1.floor.toString(),
                onValueChange = { value: String ->
                    if (value.matches(pattern)) {
                        onStateChange(state.copy(address1 = state.address1.copy(floor = if (value.isNotEmpty()) value.toInt() else 0)))
                    }
                },
                maxLines = 1,
                textStyle = MaterialTheme.typography.bodyMedium,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        Text("Uslikaj i odradi transport već danas!")
        Spacer(modifier = Modifier.width(16.dp))
        Row {
            OutlinedTextField(
                modifier = Modifier
                    .width(90.dp)
                    .height(60.dp)
                    .padding(start = 15.dp, top = 10.dp, end = 15.dp)
                    .background(Color.White, RoundedCornerShape(5.dp)),
                shape = RoundedCornerShape(5.dp),
                value = state.address1.doorCode.toString(),
                onValueChange = { value ->
                    onStateChange(state.copy(address1 = state.address1.copy(doorCode = value)))
                },
                maxLines = 1,
                textStyle = MaterialTheme.typography.bodyMedium,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )
            OutlinedTextField(
                modifier = Modifier
                    .width(90.dp)
                    .height(60.dp)
                    .padding(start = 15.dp, top = 10.dp, end = 15.dp)
                    .background(Color.White, RoundedCornerShape(5.dp)),
                shape = RoundedCornerShape(5.dp),
                value = state.address1.phoneNumber,
                onValueChange = { value ->
                    onStateChange(state.copy(address1 = state.address1.copy(phoneNumber = value)))
                },
                maxLines = 4,
                textStyle = MaterialTheme.typography.bodyMedium,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )
        }
    }
}


@Composable
fun addressDetailss2(
    state: UserRequestResponseModel,
    onStateChange: (UserRequestResponseModel) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.width(16.dp))
        Text("Početna adresa")
        Spacer(modifier = Modifier.width(16.dp))
        OutlinedTextField(
            value = state.address2.addressName,
            onValueChange = { value ->
                onStateChange(state.copy(address2 = state.address2.copy(addressName = value)))
            },
            label = { Text("Traži adresu") }
        )
        Spacer(modifier = Modifier.width(16.dp))
        Row {
            val pattern = remember { Regex("^\\d*\$") }
            OutlinedTextField(
                modifier = Modifier
                    .width(90.dp)
                    .height(60.dp)
                    .padding(start = 15.dp, top = 10.dp, end = 15.dp)
                    .background(Color.White, RoundedCornerShape(5.dp)),
                shape = RoundedCornerShape(5.dp),
                value = state.address2.floor.toString(),
                onValueChange = { value: String ->
                    if (value.matches(pattern)) {
                        onStateChange(state.copy(address2 = state.address2.copy(floor = if (value.isNotEmpty()) value.toInt() else 0)))
                    }
                },
                maxLines = 1,
                textStyle = MaterialTheme.typography.bodyMedium,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        Text("Uslikaj i odradi transport već danas!")
        Spacer(modifier = Modifier.width(16.dp))
        Row {
            OutlinedTextField(
                modifier = Modifier
                    .width(90.dp)
                    .height(60.dp)
                    .padding(start = 15.dp, top = 10.dp, end = 15.dp)
                    .background(Color.White, RoundedCornerShape(5.dp)),
                shape = RoundedCornerShape(5.dp),
                value = state.address2.doorCode.toString(),
                onValueChange = { value ->
                    onStateChange(state.copy(address2 = state.address2.copy(doorCode = value)))
                },
                maxLines = 1,
                textStyle = MaterialTheme.typography.bodyMedium,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )
            OutlinedTextField(
                modifier = Modifier
                    .width(90.dp)
                    .height(60.dp)
                    .padding(start = 15.dp, top = 10.dp, end = 15.dp)
                    .background(Color.White, RoundedCornerShape(5.dp)),
                shape = RoundedCornerShape(5.dp),
                value = state.address2.phoneNumber,
                onValueChange = { value ->
                    onStateChange(state.copy(address2 = state.address2.copy(phoneNumber = value)))
                },
                maxLines = 4,
                textStyle = MaterialTheme.typography.bodyMedium,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )
        }
    }
}


@Composable
fun SinglePhotoPicker(onPhotoPicked: (Uri?) -> Unit) {
    val context = LocalContext.current
    val file = remember { context.createImageFile() }
    val uri =
        remember { FileProvider.getUriForFile(context, "${context.packageName}.provider", file) }
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
            onClick = { singlePhotoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
            shape = RoundedCornerShape(10.dp), // This makes the button square
            colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow)
        ) {
            Image(
                painterResource(R.drawable.baseline_image_24),
                "gallery",
                modifier = Modifier.size(100.dp)
            )
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
        var imageUri by rememberSaveable {
            mutableStateOf<Uri?>(null)
        }
        if (imageUri != null) {
            AsyncImage(
                model = imageUri,
                contentDescription = "Image",
                modifier = Modifier
                    .size(220.dp)
                    .clickable(onClick = { isSheetOpen = true })
            )
        } else if (text == "Default") {
            Image(
                painter = painterResource(id = R.drawable.baseline_image_24),
                contentDescription = "Default image",
                modifier = Modifier
                    .size(220.dp)
                    .clickable(onClick = { isSheetOpen = true })
            )
        } else if (text == "User") {
            Image(
                painter = painterResource(id = R.drawable.user),
                contentDescription = "Default image",
                modifier = Modifier
                    .size(220.dp)
                    .clickable(onClick = { isSheetOpen = true })
            )
        } else {


            AsyncImage(
                model = text.toUri(),
                contentDescription = "Current Image",
                modifier = Modifier
                    .size(220.dp)
                    .clickable(onClick = { isSheetOpen = true })
            )

        }

        Box {
            if (isSheetOpen) {


                ModalBottomSheet(
                    onDismissRequest = { isSheetOpen = false },
                    sheetState = sheetState,
                    shape = RoundedCornerShape(25.dp),
                    containerColor = Color.Gray,
                    content = {
                        Column(
                            content = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    SinglePhotoPicker { uri ->
                                        if (uri != null) {
                                            imageUri = uri
                                            onImageUriReceived(uri)
                                        }
                                        isSheetOpen = false
                                    }
                                    Spacer(modifier = Modifier.width(30.dp))
                                    CameraPhotoTaker { uri ->
                                        if (uri != null) {
                                            imageUri = uri
                                            onImageUriReceived(uri)
                                        }
                                        isSheetOpen = false
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
}


@Composable
fun CameraPhotoTaker(onPhotoTaken: (Uri?) -> Unit) {
    val context = LocalContext.current
    val file = remember { context.createImageFile() }
    val uri =
        remember { FileProvider.getUriForFile(context, "${context.packageName}.provider", file) }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                onPhotoTaken(uri)
            } else {
                Toast.makeText(context, "Failed to capture image", Toast.LENGTH_SHORT).show()
            }
        }

    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
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
                val permissionCheckResult =
                    ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                    cameraLauncher.launch(uri)
                } else {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
            },
            shape = RoundedCornerShape(10.dp), // This makes the button square
            colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow)
        ) {
            Image(
                painterResource(R.drawable.baseline_camera_alt_24),
                "gallery",
                modifier = Modifier.size(100.dp)
            )
        }
    }
}
