package com.example.ui_users.read

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ui_users.R
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


@Composable
fun UpdateUserProfileDialogScreen(
    navController: NavController,
    userProfileViewModel: UserProfileViewModel = hiltViewModel(),
    onDismiss: () -> Unit
){


    var confirmEnabled by remember { mutableStateOf(false) }




        Dialog(
            onDismissRequest = { onDismiss() },
            properties = DialogProperties(usePlatformDefaultWidth = false),
            content = {
                Surface (modifier = Modifier.fillMaxSize().padding(40.dp).padding(bottom = 400.dp),
                    shape = RoundedCornerShape(15.dp)
                    ){
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {


                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Dismiss")
                        }
                    }
                    ImageInput1(userProfileViewModel.profilePicture) { uri -> userProfileViewModel.setProfilePicture(uri.toString()) }
                    TextField(
                        value = userProfileViewModel.name,
                        onValueChange = {
                            userProfileViewModel.setName(it)
                            confirmEnabled =
                                userProfileViewModel.name.isNotBlank() && userProfileViewModel.surname.isNotBlank() && userProfileViewModel.email.isNotEmpty()
                        },
                        placeholder = { Text("First name") }
                    )
                    TextField(
                        value = userProfileViewModel.surname,
                        onValueChange = {
                            userProfileViewModel.setSurname(it)

                            confirmEnabled =
                                userProfileViewModel.name.isNotBlank() && userProfileViewModel.surname.isNotBlank() && userProfileViewModel.email.isNotEmpty()
                        },
                        placeholder = { Text("Last name") }
                    )
                    TextField(
                        value = userProfileViewModel.email,
                        onValueChange = {
                            userProfileViewModel.setEmail(it)
                            confirmEnabled =
                                userProfileViewModel.name.isNotBlank() && userProfileViewModel.surname.isNotBlank() && userProfileViewModel.email.isNotEmpty()
                        },
                        placeholder = { Text("Email") }
                    )
                    Button(
                        onClick = {
                            userProfileViewModel.updateUserProfile()
                            onDismiss()

                        },
                        enabled = confirmEnabled
                    ) {
                        Text("Save")
                    }

                    }
                }
            },
             // Set dismissButton to null to make the dialog not dismissable on click outside
        )


    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageInput1(text: String, onImageUriReceived: (Uri?) -> Unit) {
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
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.DarkGray, CircleShape)
                    .clickable(onClick = { isSheetOpen = true })
            )
        }else if (text != null){
            AsyncImage(
                model = text.toUri(),
                contentDescription = "Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.DarkGray, CircleShape)
                    .clickable(onClick = { isSheetOpen = true })
            )
        } else {
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
        }

        if (isSheetOpen) {

            ModalBottomSheet(
                onDismissRequest = { isSheetOpen=false },
                sheetState = sheetState,
                shape = RoundedCornerShape(25.dp),
                content = {
                    Column(
                        content = {
//                               Spacer(modifier = Modifier.padding(16.dp))
                            Row(modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center) {
                                SinglePhotoPicker1 { uri ->
                                    if (uri != null) {
                                        imageUri=uri
                                        onImageUriReceived(uri)
                                    }
                                    isSheetOpen=false
                                }
                                CameraPhotoTaker1 { uri ->
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

            )
        }
    }
}


@Composable
fun SinglePhotoPicker1(onPhotoPicked: (Uri?) -> Unit){
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
        IconButton(
            onClick = {singlePhotoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }) {
            Image(painterResource(R.drawable.round_image_24), "gallery", modifier = Modifier.size(200.dp) )
        }

//        AsyncImage(
//            model = uri,
//            contentDescription = null,
//            modifier = Modifier.size(250.dp)
//            )
    }
}

@Composable
fun CameraPhotoTaker1(onPhotoTaken: (Uri?) -> Unit) {
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

    ImageTaker1(
        cameraLauncher = cameraLauncher,
        context = context,
        uri = uri,
        permissionLauncher = permissionLauncher,
        onPhotoTaken = onPhotoTaken // Pass the callback to ImageTaker
    )
}

@Composable
fun ImageTaker1(
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
            }
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Add Image",)
        }
    }
}
