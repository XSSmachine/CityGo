package com.example.ui_users.read

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.ui_users.R
import com.hfad.model.Loading
import com.hfad.model.Triumph
import com.hfad.model.UserProfileResponseModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


private lateinit var context: Context
private lateinit var activity: Activity
private var navController: NavHostController? = null
private lateinit var viewModel: UserProfileViewModel

@Composable
fun UpdateUserProfileDialogScreen(
    navController: NavController,
    ViewModel: UserProfileViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
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

    LaunchedEffect(Unit) {
        viewModel.viewState.observe(lifecycleOwner) { value ->
            when (value) {
                is Loading -> {
                    //Loading case
                }

                is Triumph -> {
                    when (value.data) {
                        is UserProfileResponseModel -> {
                            Log.d("PROFILE ADS", value.data.profilePicture.toString())
                            userData.value = value.data
                        }
                    }
                }

                is Error -> {}
                else -> {}
            }
        }
    }
    UpdateDialog(
        userData = userData.value,
        viewModel = viewModel,
        onStateChange = { newState -> userData.value = newState },
        onDismiss
    )
}

@Composable
fun UpdateDialog(
    userData: UserProfileResponseModel,
    viewModel: UserProfileViewModel,
    onStateChange: (UserProfileResponseModel) -> Unit,
    onDismiss: () -> Unit
) {

    var confirmEnabled by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()




    Dialog(
        onDismissRequest = {
            coroutineScope.launch {
                viewModel.getProfileDetails()
                delay(2000)
                onDismiss()
            }
        },
        properties = DialogProperties(usePlatformDefaultWidth = false),
        content = {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(40.dp)
                    .padding(bottom = 350.dp),
                shape = RoundedCornerShape(15.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {


                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Dismiss")
                        }
                    }
                    ImageInput1(userData.profilePicture.toString()) { uri ->
                        onStateChange(
                            userData.copy(
                                profilePicture = uri.toString()
                            )
                        )
                    }
                    TextField(
                        value = userData.name,
                        onValueChange = {
                            onStateChange(userData.copy(name = it))
                            confirmEnabled =
                                userData.name.isNotBlank() && userData.surname.isNotBlank() && userData.email.toString()
                                    .isNotEmpty()
                        },
                        placeholder = { Text("First name") }
                    )
                    TextField(
                        value = userData.surname,
                        onValueChange = {
                            onStateChange(userData.copy(surname = it))

                            confirmEnabled =
                                userData.name.isNotBlank() && userData.surname.isNotBlank() && userData.email.toString()
                                    .isNotEmpty()
                        },
                        placeholder = { Text("Last name") }
                    )
                    TextField(
                        value = userData.email.toString(),
                        onValueChange = {
                            onStateChange(userData.copy(email = it))
                            confirmEnabled =
                                userData.name.isNotBlank() && userData.surname.isNotBlank() && userData.email.toString()
                                    .isNotEmpty()
                        },
                        placeholder = { Text("Email") }
                    )
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                viewModel.updateUserProfile(userData)

                                Toast.makeText(
                                    context,
                                    "Profile updated successfully",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }


                        },
                        enabled = confirmEnabled
                    ) {
                        Text("Save")
                    }

                }
            }
        },
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
        } else if (text != null) {
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
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.DarkGray, CircleShape)
                    .clickable(onClick = { isSheetOpen = true })

            )
        }

        if (isSheetOpen) {

            ModalBottomSheet(
                onDismissRequest = { isSheetOpen = false },
                sheetState = sheetState,
                shape = RoundedCornerShape(25.dp),
                containerColor = Color.Gray,
                content = {
                    Column(
                        content = {
//                               Spacer(modifier = Modifier.padding(16.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                if (text != "Selfie") {
                                    SinglePhotoPicker { uri ->
                                        if (uri != null) {
                                            imageUri = uri
                                            onImageUriReceived(uri)
                                        }
                                        isSheetOpen = false
                                    }
                                    Spacer(modifier = Modifier.width(30.dp))
                                }

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