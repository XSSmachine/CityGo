package com.example.userrequest.read

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.userrequest.R
import com.example.userrequest.readAll.UserRequestItem

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ReadUserRequestScreen(
    navController: NavController,
    onUserRequestClick: (Int,String) -> Unit,
    onUserRequestButtonClick: (Int) -> Unit,
    listUserRequestViewModel: ReadUserRequestViewModel = hiltViewModel()
) {


    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit,
        block = {
            if(listUserRequestViewModel.getUserRole()=="Cygo"){
                listUserRequestViewModel.getServiceProviderOffers()
            }else{
                listUserRequestViewModel.getMyUserRequest()
            }


        })

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "My activities")
                },

            )
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            LazyColumn(
                modifier = Modifier.fillMaxHeight()
            ) {
                if(listUserRequestViewModel.getUserRole()=="Owner"){
                    itemsIndexed(listUserRequestViewModel.userRequests) { index, item ->

                        UserRequestItem(index = index, userRequest = item, onUserRequestClicked = onUserRequestClick,onUserRequestButtonClicked = onUserRequestButtonClick,listUserRequestViewModel)

                    }
                }else{
                    itemsIndexed(listUserRequestViewModel.SPuserRequests) { index, item ->

                        UserRequestItem(index = index, userRequest = item, onUserRequestClicked = onUserRequestClick,onUserRequestButtonClicked = onUserRequestButtonClick,listUserRequestViewModel)

                    }
                }

            }


        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserRequestItem(
    index: Int,
    userRequest: UserRequestListResponseModel,
    onUserRequestClicked: (Int,String) -> Unit,
    onUserRequestButtonClicked: (Int) -> Unit,
    viewModel: ReadUserRequestViewModel
) {
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
            .padding(top = 50.dp),
        shape = shape,
        onClick = { onUserRequestClicked(userRequest.id,userRequest.userId) }
    ) {
        if (viewModel.getUserRole()=="Cygo"){
            Row {
                Text(text = "Your offer is: "+viewModel.getSingleOfferStatus(userRequest.id))
            }
        }
        
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Picture
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .padding(8.dp)
            ) {
                if (userRequest.photo != null) {
                    Image(
                        painter = rememberAsyncImagePainter(userRequest.photo),
                        contentDescription = "Image",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(shape = RoundedCornerShape(8.dp))
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = "Image",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            // Texts
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
            ) {
                // Timetable
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Timetable",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = userRequest.timeTable.substringAfter(","),
                        maxLines = 1,
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 15.sp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Address name
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Address",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${userRequest.address1.addressName} | ${userRequest.address2.addressName}",
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 10.sp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Price
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Price",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Ukupna cijena ${userRequest.price}",
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                if (viewModel.checkOffersForRequest(userRequest.id) && viewModel.getUserRole()=="Owner"){
                    Button(
                        onClick = { onUserRequestButtonClicked(userRequest.id) },
                        modifier = Modifier.fillMaxWidth(),
                        colors= ButtonDefaults.buttonColors(
                            containerColor = Color.Yellow,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(text = "Select a cygo")
                    }
                }else if (viewModel.getUserRole()=="Owner"){
                    Button(
                        onClick = { /* update offer price by 5 euro */ },
                        modifier = Modifier.fillMaxWidth(),
                        colors= ButtonDefaults.buttonColors(
                            containerColor = Color.Yellow,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(text = "+5€")
                    }
                }
                // Button

            }
        }
    }
}

