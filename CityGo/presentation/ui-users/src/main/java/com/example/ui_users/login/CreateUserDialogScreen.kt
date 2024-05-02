package com.example.ui_users.login

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.play.integrity.internal.c
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@Composable
fun CreateUserDialogScreen(
    navController: NavController,
    createUserProfileViewModel: UserLoginViewModel = hiltViewModel()
){


    var confirmEnabled by remember { mutableStateOf(false) }
    AlertDialog(
        onDismissRequest = { },
        title = { Text("Enter information") },
        text = {
            Column {
                TextField(
                    value = createUserProfileViewModel.name,
                    onValueChange = {
                        createUserProfileViewModel.setName(it)
                        confirmEnabled = createUserProfileViewModel.name.isNotBlank() && createUserProfileViewModel.surname.isNotBlank()
                    },
                    label = { Text("Name") }
                )
                TextField(
                    value = createUserProfileViewModel.surname,
                    onValueChange = {
                        createUserProfileViewModel.setSurname(it)

                        confirmEnabled = createUserProfileViewModel.name.isNotBlank() && createUserProfileViewModel.surname.isNotBlank()
                    },
                    label = { Text("Surname") }
                )
                TextField(
                    value = createUserProfileViewModel.email,
                    onValueChange = { createUserProfileViewModel.setEmail(it) },
                    label = { Text("Email (optional)") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    runBlocking {
                        Log.d("VIEWMODEL",createUserProfileViewModel.phoneNumber)
                        //neradi Log.d("VIEWMODEL1",createUserProfileViewModel.userId.first())
                        val user = Firebase.auth.currentUser
                        if (user != null) {
//                                                    Log.d("LOGIN",user.uid)
//                                                    createUserProfileViewModel.cacheName(user.uid)
                            createUserProfileViewModel.setUserId(user.uid)
                        }
                        createUserProfileViewModel.createUser()
                        createUserProfileViewModel.clearAll()
                        navController.navigate("home")
                        /**
                         * Add functionality to flag user as logged in or check wheather user is logged in so it can navigate to right page,
                         * since this dialog only shows when user is coming into my app for the first time and it depends how users go about their login
                         * CASE 1 they press the profile icon and are prompted to input their phone number and enter their details in this dialog
                         * CASE 2 they go about making their request and then in middle of that they get same thing and dialog(if they dont have a profile already)
                         */
                    }
                },
                enabled = confirmEnabled
            ) {
                Text("Confirm")
            }
        },
        dismissButton = null // Set dismissButton to null to make the dialog not dismissable on click outside
    )




}



