package com.example.ui_users.login

import android.app.Activity
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.runBlocking

@Composable
fun LoginScreen(
    navController: NavController,
    createUserProfileViewModel: UserLoginViewModel = hiltViewModel()
){

    var uid = rememberSaveable {
        mutableStateOf("")
    }

    val otp = rememberSaveable {
        mutableStateOf("")
    }

    val verificationID = rememberSaveable {
        mutableStateOf("")
    }//Need this to get credentials

    val codeSent = rememberSaveable {
        mutableStateOf(false)
    }//Optional- Added just to make consistent UI

    val loading = rememberSaveable {
        mutableStateOf(false)
    }//Optional

    val context = LocalContext.current

    val mAuth: FirebaseAuth = Firebase.auth

    lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    Column {
        TextField(//import androidx.compose.material3.TextField
            enabled = !codeSent.value && !loading.value,
            value = createUserProfileViewModel.phoneNumber,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = { if (it.length <= 10) createUserProfileViewModel.setPhoneNumber(it) },
            placeholder = { Text(text = "Enter your phone number") },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            supportingText = {
                Text(
                    text = "${createUserProfileViewModel.phoneNumber.length} / 10",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                )
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        AnimatedVisibility(
            visible = !codeSent.value,
            exit = scaleOut(
                targetScale = 0.5f,
                animationSpec = tween(durationMillis = 500, delayMillis = 100)
            ),
            enter = scaleIn(
                initialScale = 0.5f,
                animationSpec = tween(durationMillis = 500, delayMillis = 100)
            )
        ) {
            Button(
                enabled = !loading.value && !codeSent.value,
                onClick = {
                    if (TextUtils.isEmpty(createUserProfileViewModel.phoneNumber) || createUserProfileViewModel.phoneNumber.length < 10) {
                        Toast.makeText(
                            context,
                            "Enter a valid phone number",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else {
                        loading.value = true
                        val number = "+385${createUserProfileViewModel.phoneNumber}"
                        createUserProfileViewModel.sendVerificationCode(number, mAuth, context as Activity, callbacks)//This is the main method to send the code after verification
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = "Generate OTP", modifier = Modifier.padding(8.dp))
            }
        }
        //otp popup upon valid phone number input
        AnimatedVisibility(
            visible = codeSent.value,
            // Add same animation here
        ) {
            Column {
                TextField(
                    enabled = !loading.value,
                    value = otp.value,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onValueChange = { if (it.length <= 6) otp.value = it },
                    placeholder = { Text(text = "Enter your otp") },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    supportingText = {
                        Text(
                            text = "${otp.value.length} / 6",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End,
                        )
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    enabled = !loading.value,
                    onClick = {
                        if (TextUtils.isEmpty(otp.value) || otp.value.length < 6) {
                            Toast.makeText(
                                context,
                                "Please enter a valid OTP",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        } else {
                            loading.value = true
                            //This is the main part where we verify the OTP
                            val credential: PhoneAuthCredential =
                                PhoneAuthProvider.getCredential(
                                    verificationID.value, otp.value
                                )//Get credential object
                            mAuth.signInWithCredential(credential)
                                .addOnCompleteListener(context as Activity) { task ->
                                    if (task.isSuccessful) {
                                        //Code after auth is successful
                                        runBlocking {
                                            if(createUserProfileViewModel.checkIfUserExists(createUserProfileViewModel.phoneNumber)){
                                                val user = Firebase.auth.currentUser
                                                if (user != null) {
                                                    Log.d("LOGIN",user.uid)
                                                    createUserProfileViewModel.getUserId(user.uid)
                                                }
                                                navController.navigate("home")
                                                Log.d("LoginScreen","Wrong route")
                                            }else{
                                                Log.d("VIEWMODEL",createUserProfileViewModel.phoneNumber)
                                                navController.navigate("dialog")

                                            }
                                            /**
                                             * Add functionality to flag user as logged in or check whether user is logged in so it can navigate to right page,
                                             * since this login scrreen depends on how users go about their login
                                             * CASE 1 they press the profile icon and are prompted to input their phone number and login
                                             * CASE 2 they go about making their request and then in middle of that they get same thing if they are not already logged in
                                             */
                                        }
                                    } else {
                                        loading.value = false
                                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                                            Toast.makeText(
                                                context,
                                                "Verification failed.." + (task.exception as FirebaseAuthInvalidCredentialsException).message,
                                                Toast.LENGTH_LONG
                                            ).show()
                                            if ((task.exception as FirebaseAuthInvalidCredentialsException).message?.contains(
                                                    "expired"
                                                ) == true
                                            ) {//If code is expired then get a chance to resend the code
                                                codeSent.value = false
                                                otp.value = ""
                                            }
                                        }
                                    }
                                }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(text = "Verify OTP", modifier = Modifier.padding(8.dp))
                }
            }
        }
    }

    if (loading.value) {
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
    }
    callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            Toast.makeText(context, "Verification successful..", Toast.LENGTH_SHORT).show()
            loading.value = false
        }

        override fun onVerificationFailed(p0: FirebaseException) {
            Toast.makeText(context, "Verification failed.. ${p0.message}", Toast.LENGTH_LONG)
                .show()
            loading.value = false
        }

        override fun onCodeSent(
            verificationId: String,
            p1: PhoneAuthProvider.ForceResendingToken
        ) {
            super.onCodeSent(verificationId, p1)
            verificationID.value = verificationId
            codeSent.value = true
            loading.value = false
        }
    }

}

