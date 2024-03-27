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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
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
    createUserRequestViewModel: CreateUserRequestViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    var file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.packageName+".provider",
        file

    )

    var capturedImageUris by remember {
        mutableStateOf<List<Uri>>(emptyList())
    }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            capturedImageUris = capturedImageUris + uri
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
                title = {
                    Text(text = "CYGO")
                }
            )
        }
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)) {
            Text("Jednostavan prijevoz. Prebaci bilo što od točke A do točke B. Dogovor u samo nekoliko minuta.")

            Spacer(modifier = Modifier.width(16.dp))

            ImageTaker(capturedImageUris,cameraLauncher,context,uri,permissionLauncher)

            Spacer(modifier = Modifier.width(16.dp))

            Text("Uslikaj i odradi transport već danas!")

            Spacer(modifier = Modifier.width(16.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth().height(120.dp)
                    .padding(start = 15.dp, top = 10.dp, end = 15.dp)
                    .background(Color.White, RoundedCornerShape(5.dp)),
                shape = RoundedCornerShape(5.dp),
                value = createUserRequestViewModel.description,
                onValueChange = { value ->
                                createUserRequestViewModel.onDescriptionChange(value)
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                maxLines = 4,
                textStyle = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.width(16.dp))

            Button(onClick = {
                runBlocking {
//                    createUserRequestViewModel.createContact()
                    navController.navigate("details")

                    //Prebaci na iduci screen
                }
            }) {
                Text(text = "Idući korak")
            }

        }

    }

}


@Composable
fun ImageTaker(
    capturedImageUris: List<Uri>,
    cameraLauncher: ManagedActivityResultLauncher<Uri, Boolean>,
    context: Context,
    uri: Uri,
    permissionLauncher: ManagedActivityResultLauncher<String, Boolean>
) {
    Column {
        capturedImageUris.forEachIndexed { index, uri ->
            Image(
                modifier = Modifier.padding(16.dp, 16.dp),
                painter = rememberImagePainter(uri),
                contentDescription = "Image $index"
            )
        }

        Button(onClick = {
            val permissionCheckResult = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
            if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                cameraLauncher.launch(uri)
            } else {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Add Image"
            )
        }
    }
}


