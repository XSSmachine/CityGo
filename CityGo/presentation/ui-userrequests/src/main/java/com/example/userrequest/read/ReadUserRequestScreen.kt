package com.example.userrequest.read

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.R
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ReadUserRequestScreen(
    navController: NavController,
    listUserRequestViewModel: ReadUserRequestViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit,
        block = {
            listUserRequestViewModel.getContacts()
        })

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Lista oglasa")
                },
                actions = {
                    Button(onClick = {
                        navController.navigate("picture")
                    }) {
                        Text(text = "Novi")
                    }
                }
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
                items(listUserRequestViewModel.contacts) { item ->
                    Row(modifier = Modifier.padding(2.dp)) {
                        Text(text = item.id)
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(text = item.address1)

                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = item.address2)
                        Spacer(modifier = Modifier.width(16.dp))

                        if (item.imageUri.path?.isNotEmpty() == true) {
                            Image(
                                modifier = Modifier.padding(16.dp, 16.dp)
                                    .width(98.dp).height(98.dp),
                                painter = rememberImagePainter(item.imageUri),
                                contentDescription = "Image"
                            )
                        } else {
                            Icon(
                                Icons.Default.Info,
                                contentDescription = "Image"
                            )
                        }
                    }
                }
            }


        }

    }

}