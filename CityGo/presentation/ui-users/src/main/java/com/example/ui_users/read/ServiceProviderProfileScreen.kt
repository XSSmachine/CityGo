package com.example.ui_users.read

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.ui_users.R
import kotlinx.coroutines.runBlocking

@Composable
fun ServiceProviderProfileScreen(
    navController: NavController,
    userViewModel: UserProfileViewModel = hiltViewModel()
){
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    )
    {
        Column(
            modifier = Modifier
                .width(200.dp)
                .height(390.dp)
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            CreateImageProfile(userViewModel)
            CreateInfo(userViewModel,navController)
            Divider()
        }
    }

}


@Composable
private fun CreateImageProfile(userViewModel: UserProfileViewModel) {
    Surface(
        modifier = Modifier
            .size(154.dp)
            .padding(5.dp),
        shape = CircleShape,
        border = BorderStroke(0.5.dp, Color.LightGray),
        tonalElevation = 4.dp,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
    ) {
        Log.d("VIDIM",userViewModel.providerState.profilePicture.toString())
        if (userViewModel.providerState.profilePicture.isNotEmpty()) {
            Image(
                modifier = Modifier
                    .padding(16.dp, 16.dp)
                    .width(98.dp)
                    .height(98.dp),
                painter = rememberAsyncImagePainter(userViewModel.providerState.profilePicture.toUri()),
                contentDescription = "Image"
            )
        } else {
            Image(
                modifier = Modifier.padding(16.dp, 16.dp),
                painter = painterResource(id = R.drawable.ic_image),
                contentDescription = "Image"
            )
        }

    }
}

@Composable
private fun CreateInfo(userViewModel: UserProfileViewModel,navController: NavController) {
    Column(
        modifier = Modifier
            .padding(5.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            fontSize = 30.sp,
            style = MaterialTheme.typography.headlineMedium,
            text = userViewModel.providerState.name
        )

        Text(
            text = userViewModel.providerState.surname,
            modifier = Modifier.padding(3.dp),
            style = MaterialTheme.typography.headlineMedium,
        )

        Text(
            text = userViewModel.providerState.dateOfBirth,
            modifier = Modifier.padding(3.dp),
            style = MaterialTheme.typography.headlineMedium,
        )

        Text(
            color = Color.Blue,
            text = userViewModel.providerState.email,
            modifier = Modifier.padding(3.dp),
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = userViewModel.providerState.stars.toString()+"⭐",
            modifier = Modifier.padding(3.dp),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.padding(12.dp))
        Button(onClick = {
            runBlocking { userViewModel.clearUserId() }
            navController.navigate("login")


        },contentPadding = PaddingValues(5.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black
            )

        ) {

            Text(text = "Log out")
        }

    }
}

