package com.example.userrequest.create

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.userrequest.R
import com.example.userrequest.update.ImageInput
import kotlinx.coroutines.runBlocking
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CreatePictureUserRequestScreen(
    navController: NavController,
    createUserRequestViewModel: CreateUserRequestViewModel = hiltViewModel(),
    navigateUp:() ->Unit,
) {

    val context = LocalContext.current
    var file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.packageName+".provider",
        file

    )


    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            createUserRequestViewModel.setCapturedImageUri(uri)
            createUserRequestViewModel.onImageCaptured(uri)
        } else {
            Toast.makeText(context, "Failed to capture image", Toast.LENGTH_SHORT).show()
        }
    }


    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()){
        if(it){
            Toast.makeText(context, "Permission granted!", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uri)
        }else{
            Toast.makeText(context, "Permission denied!", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar (
                navigationIcon = { IconButton(onClick = navigateUp) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                },
                title = {
//                    Spacer(modifier = Modifier.height(15.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Progress(0)
                    }
                    Row (
                        modifier = Modifier.fillMaxWidth().padding(top=40.dp, bottom = 4.dp),
                        horizontalArrangement = Arrangement.Center
                    ){
                        Text(text = "Photo",style = TextStyle.Default.copy(fontSize = 11.sp), color = Color.Gray)
                        Spacer(modifier = Modifier.width(125.dp))
                        Text(text = "Address",style = TextStyle.Default.copy(fontSize = 11.sp), color = Color.Gray)
                        Spacer(modifier = Modifier.width(102.dp))
                        Text(text = "Set a price",style = TextStyle.Default.copy(fontSize = 11.sp), color = Color.Gray)
                    }


                }
            )
        }
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)) {
            Text("Jednostavan prijevoz. Prebaci bilo što od točke A do točke B. Dogovor u samo nekoliko minuta.")

            Spacer(modifier = Modifier.size(16.dp))


            ImageInput("Default") {uri ->
                // Handle the received URI here, for example, update selfiePicture state
                if (uri != null) {
                    createUserRequestViewModel.onImageCaptured(uri)
                }
            }
//            ImageTaker(capturedImageUri,cameraLauncher,context,uri,permissionLauncher)

            Spacer(modifier = Modifier.size(16.dp))

            Text("Uslikaj i odradi transport već danas!", textAlign = TextAlign.Left)

            Spacer(modifier = Modifier.width(16.dp))

//            OutlinedTextField(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(120.dp)
//                    .padding(start = 15.dp, top = 10.dp, end = 15.dp)
//                    .background(Color.White, RoundedCornerShape(5.dp)),
//                shape = RoundedCornerShape(5.dp),
//                value = createUserRequestViewModel.description,
//                onValueChange = { value ->
//                                createUserRequestViewModel.onDescriptionChange(value)
//                },
//                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
//                maxLines = 4,
//                textStyle = MaterialTheme.typography.bodyMedium
//            )

            OutlinedTextField(
                value = createUserRequestViewModel.description,
                onValueChange = { value ->
                    if(value.length <= 300){
                        createUserRequestViewModel.onDescriptionChange(value)
                    }
                     },
                placeholder = { Text("Description, for example, size, where to find it, additional info etc.") },
                modifier=Modifier.fillMaxWidth().height(220.dp).padding(top=30.dp),
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

            Spacer(modifier = Modifier.size(100.dp))

            Button(onClick = {
                runBlocking {
//                    createUserRequestViewModel.createContact()
                    navController.navigate("details")

                    //Prebaci na iduci screen
                }
            },
                modifier = Modifier.fillMaxWidth().padding(top = 5.dp).align(Alignment.End), // Make the button fill the maximum width
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Yellow, // Set background color to yellow
                    contentColor = Color.Black // Set text color to black
                )

            ) {
                Text(text = "Idući korak",
                    style = TextStyle.Default.copy(fontSize = 20.sp))
            }

        }

    }

}



@Composable
fun Progress(step:Int) {
    val stepCount = 3

    Row(
        modifier = Modifier
            .fillMaxWidth()
            ,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(stepCount) { index ->
            Circle(modifier = Modifier.size(30.dp)) {
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
            .size(24.dp)
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




