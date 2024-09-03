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
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.runBlocking

@Composable
fun CreateUserDialogScreen(
    navController: NavController,
    createUserProfileViewModel: UserLoginViewModel = hiltViewModel()
) {


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
                        confirmEnabled =
                            createUserProfileViewModel.name.isNotBlank() && createUserProfileViewModel.surname.isNotBlank()
                    },
                    label = { Text("Name") }
                )
                TextField(
                    value = createUserProfileViewModel.surname,
                    onValueChange = {
                        createUserProfileViewModel.setSurname(it)

                        confirmEnabled =
                            createUserProfileViewModel.name.isNotBlank() && createUserProfileViewModel.surname.isNotBlank()
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
                        val user = Firebase.auth.currentUser
                        if (user != null) {
                            createUserProfileViewModel.setUserId(user.uid)
                        }
                        createUserProfileViewModel.createUser()
                        createUserProfileViewModel.clearAll()
                        navController.navigate("home")
                    }
                },
                enabled = confirmEnabled
            ) {
                Text("Confirm")
            }
        },
        dismissButton = null
    )


}



