package com.example.userrequest.create

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.userrequest.R
import com.example.userrequest.update.ImageInput
import kotlinx.coroutines.runBlocking
import java.util.Objects

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CreatePictureUserRequestScreen(
    navController: NavController,
    createUserRequestViewModel: CreateUserRequestViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
) {

    val toastPicMsg = stringResource(id = R.string.toast_picture_msg)
    val toastCameraGranted = stringResource(id = R.string.toast_camera_grant)
    val toastCameraDenied = stringResource(id = R.string.toast_camera_deny)
    val context = LocalContext.current
    val isFormValid by createUserRequestViewModel.isFormValid1.collectAsState()
    var file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.packageName + ".provider",
        file

    )

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                createUserRequestViewModel.setCapturedImageUri(uri)
                createUserRequestViewModel.onImageCaptured(uri)
            } else {
                Toast.makeText(context, toastPicMsg, Toast.LENGTH_SHORT).show()
            }
        }


    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                Toast.makeText(context, toastCameraGranted, Toast.LENGTH_SHORT).show()
                cameraLauncher.launch(uri)
            } else {
                Toast.makeText(context, toastCameraDenied, Toast.LENGTH_SHORT).show()
            }
        }

    Scaffold(
        topBar = {
            ProgressTopAppBar(progress = 0, navigateUp = navigateUp)
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(2.responsiveHeight())
                .verticalScroll(rememberScrollState())
                .imePadding()
        ) {
            Spacer(modifier = Modifier.size(6.responsiveHeight()))
            Text(stringResource(id = R.string.picture_description))

            Spacer(modifier = Modifier.size(2.responsiveHeight()))

            ImageInput("Default") { uri ->
                if (uri != null) {
                    createUserRequestViewModel.onImageCaptured(uri)
                }
            }

            Spacer(modifier = Modifier.size(2.responsiveHeight()))

            Text(stringResource(id = R.string.pricture_hint), textAlign = TextAlign.Left)

            Spacer(modifier = Modifier.width(2.responsiveHeight()))


            OutlinedTextField(
                value = createUserRequestViewModel.description,
                onValueChange = { value ->
                    if (value.length <= 300) {
                        createUserRequestViewModel.onDescriptionChange(value)
                    }
                },
                placeholder = { Text(stringResource(id = R.string.picture_desc_hint)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.responsiveHeight())
                    .padding(top = 30.dp),
                singleLine = false,
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = Color.Black,
                    errorCursorColor = Color.Red,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    disabledTextColor = Color.Black,
                    unfocusedPlaceholderColor = Color.Gray,
                    focusedPlaceholderColor = Color.Gray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    containerColor = Color.LightGray,
                ),
                maxLines = 10,
                supportingText = {
                    Text(
                        text = "${createUserRequestViewModel.description.length} / 300 ",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                    )
                }

            )

            Spacer(modifier = Modifier.size(3.responsiveHeight()))

            Button(
                onClick = {
                    runBlocking {
                        navController.navigate("details")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp)
                    .align(Alignment.End), // Make the button fill the maximum width
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Yellow, // Set background color to yellow
                    contentColor = Color.Black // Set text color to black
                ),
                enabled = isFormValid

            ) {
                Text(
                    text = stringResource(id = R.string.btn_next_text),
                    style = TextStyle.Default.copy(fontSize = 20.sp)
                )
            }

        }

    }

}


@Composable
fun Progress(step: Int) {
    val stepCount = 3

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(stepCount) { index ->
            Circle(modifier = Modifier.size(4.responsiveHeight())) {
                if (index < step) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null)
                }
            }
            if (index < stepCount - 1)
                Line(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun Circle(
    modifier: Modifier,
    content: @Composable (BoxScope.() -> Unit) = { }
) {
    Box(
        modifier = modifier
            .size(4.responsiveHeight())
            .drawBehind {

                drawCircle(color = Color.Yellow)
            },
        contentAlignment = Alignment.Center,
        content = content
    )
}

@Composable
private fun Line(modifier: Modifier) {
    Canvas(modifier = modifier) {
        drawLine(
            color = Color.Yellow,
            start = Offset.Zero,
            end = Offset(size.width, 0f),
            strokeWidth = 3.dp.toPx()
        )
    }
}

@Composable
fun Int.responsiveWidth(): Dp {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    return (this * screenWidth.value / 100).dp
}

@Composable
fun Int.responsiveHeight(): Dp {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    return (this * screenHeight.value / 100).dp
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressTopAppBar(
    progress: Int,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        },
        title = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Progress(progress)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.progressbar_photo),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(16.responsiveWidth()))
                    Text(
                        text = stringResource(id = R.string.progressbar_address),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(24.responsiveWidth()))
                    Text(
                        text = stringResource(id = R.string.progressbar_price),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                }
            }
        },
        modifier = Modifier.height(64.dp)
    )

}
