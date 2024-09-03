package com.example.ui_users.read

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.ui_users.R
import com.hfad.model.Loading
import com.hfad.model.ServiceProviderProfileResponseModel
import com.hfad.model.Triumph
import kotlinx.coroutines.runBlocking

private lateinit var context: Context
private lateinit var activity: Activity
private var navController: NavHostController? = null
private lateinit var viewModel: UserProfileViewModel

@Composable
fun ServiceProviderProfileScreen(
    navController: NavController,
    ViewModel: UserProfileViewModel = hiltViewModel(),
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {

    context = LocalContext.current
    viewModel = ViewModel
    activity = ((LocalContext.current as? Activity)!!)

    val userData = remember {
        mutableStateOf(
            ServiceProviderProfileState()
        )
    }

    LaunchedEffect(Unit) {
        viewModel.getServiceProviderProfileDetails()
        viewModel.providerViewState.observe(lifecycleOwner) { value ->
            when (value) {
                is Loading -> {
                    //Loading case
                }

                is Triumph -> {
                    when (value.data) {
                        is ServiceProviderProfileResponseModel -> {
                            userData.value = value.data.toServiceProviderProfileState()
                        }
                    }
                }

                is Error -> {}
                else -> {}
            }
        }
    }
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
            CreateImageProfile(userData.value)
            CreateInfo(userData.value, viewModel, navController)
            Divider()
        }
    }

}


@Composable
private fun CreateImageProfile(userData: ServiceProviderProfileState) {
    Surface(
        modifier = Modifier
            .size(154.dp)
            .padding(5.dp),
        shape = CircleShape,
        border = BorderStroke(0.5.dp, Color.LightGray),
        tonalElevation = 4.dp,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
    ) {
        Log.d("VIDIM", userData.profilePicture.toString())
        if (userData.profilePicture.isNotEmpty()) {
            AsyncImage(
                model = userData.profilePicture.toUri(),
                contentDescription = "user image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(10.responsiveHeight())
                    .clip(CircleShape)
                    .border(2.dp, Color.DarkGray, CircleShape)
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
private fun CreateInfo(
    userData: ServiceProviderProfileState,
    viewModel: UserProfileViewModel,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .padding(5.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            fontSize = 30.sp,
            style = MaterialTheme.typography.headlineMedium,
            text = userData.name
        )

        Text(
            text = userData.surname,
            modifier = Modifier.padding(3.dp),
            style = MaterialTheme.typography.headlineMedium,
        )

        Text(
            text = userData.dateOfBirth,
            modifier = Modifier.padding(3.dp),
            style = MaterialTheme.typography.headlineMedium,
        )

        Text(
            color = Color.Blue,
            text = userData.email,
            modifier = Modifier.padding(3.dp),
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = userData.stars.toString() + "⭐",
            modifier = Modifier.padding(3.dp),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.padding(12.dp))
        Button(
            onClick = {
                runBlocking { viewModel.clearUserId() }
                navController.navigate("login")


            }, contentPadding = PaddingValues(5.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black
            )

        ) {

            Text(text = stringResource(id = R.string.logout_text))
        }

    }
}

