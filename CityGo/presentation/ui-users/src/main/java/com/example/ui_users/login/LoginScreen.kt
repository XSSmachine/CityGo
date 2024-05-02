package com.example.ui_users.login

import android.app.Activity
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ui_users.R
import com.google.android.play.integrity.internal.i
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


    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFE1E355))) {
        Row(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "CYGO",
                fontWeight = FontWeight.Bold,
                fontSize = 64.sp,
                modifier = Modifier
                    .padding(start = 16.dp,)
                    .align(Alignment.Top)

            )
            Spacer(modifier = Modifier.width(20.dp))
            Image(painter = painterResource(id = R.drawable.relax), contentDescription = "Home Icon", modifier = Modifier.padding(top = 20.dp))
        }
        Spacer(modifier = Modifier.height(30.dp))
        // Input field with icon prefix
        TextField(
            enabled = !codeSent.value && !loading.value,
            value = createUserProfileViewModel.phoneNumber ,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = {if (it.length <= 9) createUserProfileViewModel.setPhoneNumber(it)},
            leadingIcon = { Image(painter = painterResource(id = R.drawable.hr), contentDescription = "Phone Icon") },
            label = { Text("Phone Number") },
            prefix = {Text(text = "+385", color = Color.Black)},
            modifier = Modifier
                .padding(6.dp)
                .fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.DarkGray,
                disabledTextColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Gray,
                unfocusedIndicatorColor = Color.Black,
                disabledIndicatorColor = Color.Transparent)
        )
        // Button below input field
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
                onClick = { if (TextUtils.isEmpty(createUserProfileViewModel.phoneNumber) || createUserProfileViewModel.phoneNumber.length < 9) {
                    Toast.makeText(
                        context,
                        "Enter a valid phone number",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {
                    loading.value = true
                    val number = "+3850${createUserProfileViewModel.phoneNumber}"
                    createUserProfileViewModel.sendVerificationCode(number, mAuth, context as Activity, callbacks)//This is the main method to send the code after verification
                } },
                modifier = Modifier
                    .padding(6.dp)
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.LightGray
                )
            ) {
                Text("Pošalji kod", color = Color.Black)
            }}

        AnimatedVisibility(
            visible = codeSent.value,
            // Add same animation here
        ) {
            Column (modifier = Modifier.wrapContentHeight()){
                TextField(
                    enabled = !loading.value,
                    value = otp.value,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onValueChange = { if (it.length <= 6) otp.value = it },
                    placeholder = { Text(text = "Enter your otp") },
                    modifier = Modifier
                        .padding(6.dp)
                        .fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.DarkGray,
                        disabledTextColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Gray,
                        unfocusedIndicatorColor = Color.Black,
                        disabledIndicatorColor = Color.Transparent),
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
                                            val user = Firebase.auth.currentUser
                                            Log.d("PHONE",createUserProfileViewModel.checkIfUserExists(createUserProfileViewModel.phoneNumber).toString())
                                            if(createUserProfileViewModel.checkIfUserExists(createUserProfileViewModel.phoneNumber)==true){
                                                navController.navigate("home")
                                                if (user != null) {
//                                                    Log.d("LOGIN",user.uid)
//                                                    createUserProfileViewModel.cacheName(user.uid)
                                                    createUserProfileViewModel.setUserId(user.uid)
                                                }
                                            }else{
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
                        .padding(6.dp)
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.LightGray
                    )
                ) {
                    Text(text = "Verify", modifier = Modifier.padding(4.dp))
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
    // Icons below the button
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {

    }
    // Teal eclipse shape with text in the bottom right corner
    Box(

        modifier = Modifier
            .fillMaxSize().padding(top = 325.dp)

    ) {

        Image(painter = painterResource(id = R.drawable.moving), contentDescription = "Home Icon", modifier = Modifier.offset(x = 50.dp, y = 110.dp))

        Image(painter = painterResource(id = R.drawable.mobile_content), contentDescription = "Home Icon", modifier = Modifier.offset(x = 253.dp, y = 178.dp))

        Canvas(modifier = Modifier
            // I posted this to show that size of Canvas doesn't matter
            // If you don't need to use size or center params
            .size(0.dp)
            .offset(x = (460).dp, y = (500).dp)
            .align(alignment = Alignment.BottomStart),
            onDraw = {
                drawCircle(color = Color(0xFF55DCE3), radius = 716.dp.toPx())
            })
        Image(painter = painterResource(id = R.drawable.order_delivered), contentDescription = "Home Icon", modifier = Modifier.offset(x = 26.dp, y = 250.dp))
        Image(painter = painterResource(id = R.drawable.delivery_truck), contentDescription = "Home Icon", modifier = Modifier.offset(x = 200.dp, y = 360.dp))
        Text(
            text = "City Go",
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 16.dp)
        )
    }
}
//    Column {
//        TextField(//import androidx.compose.material3.TextField
//            enabled = !codeSent.value && !loading.value,
//            value = createUserProfileViewModel.phoneNumber,
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//            onValueChange = { if (it.length <= 10) createUserProfileViewModel.setPhoneNumber(it) },
//            placeholder = { Text(text = "Enter your phone number") },
//            modifier = Modifier
//                .padding(16.dp)
//                .fillMaxWidth(),
//            supportingText = {
//                Text(
//                    text = "${createUserProfileViewModel.phoneNumber.length} /  ",
//                    modifier = Modifier.fillMaxWidth(),
//                    textAlign = TextAlign.End,
//                )
//            }
//        )
//
//        Spacer(modifier = Modifier.height(10.dp))
//
//        AnimatedVisibility(
//            visible = !codeSent.value,
//            exit = scaleOut(
//                targetScale = 0.5f,
//                animationSpec = tween(durationMillis = 500, delayMillis = 100)
//            ),
//            enter = scaleIn(
//                initialScale = 0.5f,
//                animationSpec = tween(durationMillis = 500, delayMillis = 100)
//            )
//        ) {
//            Button(
//                enabled = !loading.value && !codeSent.value,
//                onClick = {
//                    if (TextUtils.isEmpty(createUserProfileViewModel.phoneNumber) || createUserProfileViewModel.phoneNumber.length < 10) {
//                        Toast.makeText(
//                            context,
//                            "Enter a valid phone number",
//                            Toast.LENGTH_SHORT
//                        )
//                            .show()
//                    } else {
//                        loading.value = true
//                        val number = "+385${createUserProfileViewModel.phoneNumber}"
//                        createUserProfileViewModel.sendVerificationCode(number, mAuth, context as Activity, callbacks)//This is the main method to send the code after verification
//                    }
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp)
//            ) {
//                Text(text = "Generate OTP", modifier = Modifier.padding(8.dp))
//            }
//        }
//        //otp popup upon valid phone number input
//        AnimatedVisibility(
//            visible = codeSent.value,
//            // Add same animation here
//        ) {
//            Column {
//                TextField(
//                    enabled = !loading.value,
//                    value = otp.value,
//                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                    onValueChange = { if (it.length <= 6) otp.value = it },
//                    placeholder = { Text(text = "Enter your otp") },
//                    modifier = Modifier
//                        .padding(16.dp)
//                        .fillMaxWidth(),
//                    supportingText = {
//                        Text(
//                            text = "${otp.value.length} / 6",
//                            modifier = Modifier.fillMaxWidth(),
//                            textAlign = TextAlign.End,
//                        )
//                    }
//                )
//
//                Spacer(modifier = Modifier.height(10.dp))
//
//                Button(
//                    enabled = !loading.value,
//                    onClick = {
//                        if (TextUtils.isEmpty(otp.value) || otp.value.length < 6) {
//                            Toast.makeText(
//                                context,
//                                "Please enter a valid OTP",
//                                Toast.LENGTH_SHORT
//                            )
//                                .show()
//                        } else {
//                            loading.value = true
//                            //This is the main part where we verify the OTP
//                            val credential: PhoneAuthCredential =
//                                PhoneAuthProvider.getCredential(
//                                    verificationID.value, otp.value
//                                )//Get credential object
//                            mAuth.signInWithCredential(credential)
//                                .addOnCompleteListener(context as Activity) { task ->
//                                    if (task.isSuccessful) {
//                                        //Code after auth is successful
//                                        runBlocking {
//                                            val user = Firebase.auth.currentUser
//                                            Log.d("PHONE",createUserProfileViewModel.checkIfUserExists(createUserProfileViewModel.phoneNumber).toString())
//                                            if(createUserProfileViewModel.checkIfUserExists(createUserProfileViewModel.phoneNumber)==true){
//                                                navController.navigate("home")
//                                                if (user != null) {
////                                                    Log.d("LOGIN",user.uid)
////                                                    createUserProfileViewModel.cacheName(user.uid)
//                                                    createUserProfileViewModel.setUserId(user.uid)
//                                                }
//                                            }else{
//                                                navController.navigate("dialog")
//
//                                            }
//                                            /**
//                                             * Add functionality to flag user as logged in or check whether user is logged in so it can navigate to right page,
//                                             * since this login scrreen depends on how users go about their login
//                                             * CASE 1 they press the profile icon and are prompted to input their phone number and login
//                                             * CASE 2 they go about making their request and then in middle of that they get same thing if they are not already logged in
//                                             */
//                                        }
//                                    } else {
//                                        loading.value = false
//                                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
//                                            Toast.makeText(
//                                                context,
//                                                "Verification failed.." + (task.exception as FirebaseAuthInvalidCredentialsException).message,
//                                                Toast.LENGTH_LONG
//                                            ).show()
//                                            if ((task.exception as FirebaseAuthInvalidCredentialsException).message?.contains(
//                                                    "expired"
//                                                ) == true
//                                            ) {//If code is expired then get a chance to resend the code
//                                                codeSent.value = false
//                                                otp.value = ""
//                                            }
//                                        }
//                                    }
//                                }
//                        }
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp)
//                ) {
//                    Text(text = "Verify OTP", modifier = Modifier.padding(8.dp))
//                }
//            }
//        }
//    }
//
//    if (loading.value) {
//        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
//    }
//    callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
//            Toast.makeText(context, "Verification successful..", Toast.LENGTH_SHORT).show()
//            loading.value = false
//        }
//
//        override fun onVerificationFailed(p0: FirebaseException) {
//            Toast.makeText(context, "Verification failed.. ${p0.message}", Toast.LENGTH_LONG)
//                .show()
//            loading.value = false
//        }
//
//        override fun onCodeSent(
//            verificationId: String,
//            p1: PhoneAuthProvider.ForceResendingToken
//        ) {
//            super.onCodeSent(verificationId, p1)
//            verificationID.value = verificationId
//            codeSent.value = true
//            loading.value = false
//        }
//    }



//@Composable
//fun JetpackComposeSkeleton() {
//    Column(modifier = Modifier
//        .fillMaxSize()
//        .background(Color(0xFFE1E355))) {
//        Row(modifier = Modifier.padding(16.dp)) {
//            Text(
//                text = "CYGO",
//                fontWeight = FontWeight.Bold,
//                fontSize = 64.sp,
//                modifier = Modifier
//                    .padding(start = 16.dp,)
//                    .align(Alignment.Top)
//
//            )
//            Spacer(modifier = Modifier.width(20.dp))
//            Image(painter = painterResource(id = R.drawable.relax), contentDescription = "Home Icon", modifier = Modifier.padding(top = 20.dp))
//        }
//
//        // Input field with icon prefix
//        TextField(
//            enabled = !codeSent.value && !loading.value,
//            value = createUserProfileViewModel.phoneNumber ,
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//            onValueChange = {if (it.length <= 9) createUserProfileViewModel.setPhoneNumber(it)},
//            leadingIcon = { Image(painter = painterResource(id = R.drawable.hr), contentDescription = "Phone Icon") },
//            label = { Text("Phone Number") },
//            prefix = {Text(text = "+385", color = Color.Black)},
//            modifier = Modifier
//                .padding(16.dp)
//                .fillMaxWidth(),
//            colors = TextFieldDefaults.colors(
//                focusedTextColor = Color.DarkGray,
//                disabledTextColor = Color.Transparent,
//                focusedContainerColor = Color.Transparent,
//                unfocusedContainerColor = Color.Transparent,
//                focusedIndicatorColor = Color.Gray,
//                unfocusedIndicatorColor = Color.Black,
//                disabledIndicatorColor = Color.Transparent)
//        )
//        // Button below input field
//        AnimatedVisibility(
//            visible = !codeSent.value,
//            exit = scaleOut(
//                targetScale = 0.5f,
//                animationSpec = tween(durationMillis = 500, delayMillis = 100)
//            ),
//            enter = scaleIn(
//                initialScale = 0.5f,
//                animationSpec = tween(durationMillis = 500, delayMillis = 100)
//            )
//        ) {
//        Button(
//            enabled = !loading.value && !codeSent.value,
//            onClick = { if (TextUtils.isEmpty(createUserProfileViewModel.phoneNumber) || createUserProfileViewModel.phoneNumber.length < 10) {
//                Toast.makeText(
//                    context,
//                    "Enter a valid phone number",
//                    Toast.LENGTH_SHORT
//                )
//                    .show()
//            } else {
//                loading.value = true
//                val number = "+3850${createUserProfileViewModel.phoneNumber}"
//                createUserProfileViewModel.sendVerificationCode(number, mAuth, context as Activity, callbacks)//This is the main method to send the code after verification
//            } },
//            modifier = Modifier
//                .padding(16.dp)
//                .align(Alignment.CenterHorizontally)
//                .fillMaxWidth(),
//            colors = ButtonDefaults.buttonColors(
//                containerColor = Color.LightGray
//            )
//        ) {
//            Text("Pošalji kod", color = Color.Black)
//        }}
//
//        AnimatedVisibility(
//            visible = codeSent.value,
//            // Add same animation here
//        ) {
//            Column {
//                TextField(
//                    enabled = !loading.value,
//                    value = otp.value,
//                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                    onValueChange = { if (it.length <= 6) otp.value = it },
//                    placeholder = { Text(text = "Enter your otp") },
//                    modifier = Modifier
//                        .padding(16.dp)
//                        .fillMaxWidth(),
//                    supportingText = {
//                        Text(
//                            text = "${otp.value.length} / 6",
//                            modifier = Modifier.fillMaxWidth(),
//                            textAlign = TextAlign.End,
//                        )
//                    }
//                )
//
//                Spacer(modifier = Modifier.height(10.dp))
//
//                Button(
//                    enabled = !loading.value,
//                    onClick = {
//                        if (TextUtils.isEmpty(otp.value) || otp.value.length < 6) {
//                            Toast.makeText(
//                                context,
//                                "Please enter a valid OTP",
//                                Toast.LENGTH_SHORT
//                            )
//                                .show()
//                        } else {
//                            loading.value = true
//                            //This is the main part where we verify the OTP
//                            val credential: PhoneAuthCredential =
//                                PhoneAuthProvider.getCredential(
//                                    verificationID.value, otp.value
//                                )//Get credential object
//                            mAuth.signInWithCredential(credential)
//                                .addOnCompleteListener(context as Activity) { task ->
//                                    if (task.isSuccessful) {
//                                        //Code after auth is successful
//                                        runBlocking {
//                                            val user = Firebase.auth.currentUser
//                                            Log.d("PHONE",createUserProfileViewModel.checkIfUserExists(createUserProfileViewModel.phoneNumber).toString())
//                                            if(createUserProfileViewModel.checkIfUserExists(createUserProfileViewModel.phoneNumber)==true){
//                                                navController.navigate("home")
//                                                if (user != null) {
////                                                    Log.d("LOGIN",user.uid)
////                                                    createUserProfileViewModel.cacheName(user.uid)
//                                                    createUserProfileViewModel.setUserId(user.uid)
//                                                }
//                                            }else{
//                                                navController.navigate("dialog")
//
//                                            }
//                                            /**
//                                             * Add functionality to flag user as logged in or check whether user is logged in so it can navigate to right page,
//                                             * since this login scrreen depends on how users go about their login
//                                             * CASE 1 they press the profile icon and are prompted to input their phone number and login
//                                             * CASE 2 they go about making their request and then in middle of that they get same thing if they are not already logged in
//                                             */
//                                        }
//                                    } else {
//                                        loading.value = false
//                                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
//                                            Toast.makeText(
//                                                context,
//                                                "Verification failed.." + (task.exception as FirebaseAuthInvalidCredentialsException).message,
//                                                Toast.LENGTH_LONG
//                                            ).show()
//                                            if ((task.exception as FirebaseAuthInvalidCredentialsException).message?.contains(
//                                                    "expired"
//                                                ) == true
//                                            ) {//If code is expired then get a chance to resend the code
//                                                codeSent.value = false
//                                                otp.value = ""
//                                            }
//                                        }
//                                    }
//                                }
//                        }
//                    },
//                    modifier = Modifier
//                        .padding(16.dp)
//                        .align(Alignment.CenterHorizontally)
//                        .fillMaxWidth(),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Color.LightGray
//                    )
//                ) {
//                    Text(text = "Verify", modifier = Modifier.padding(8.dp))
//                }
//            }
//        }
//    }
//
//    if (loading.value) {
//        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
//    }
//    callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
//            Toast.makeText(context, "Verification successful..", Toast.LENGTH_SHORT).show()
//            loading.value = false
//        }
//
//        override fun onVerificationFailed(p0: FirebaseException) {
//            Toast.makeText(context, "Verification failed.. ${p0.message}", Toast.LENGTH_LONG)
//                .show()
//            loading.value = false
//        }
//
//        override fun onCodeSent(
//            verificationId: String,
//            p1: PhoneAuthProvider.ForceResendingToken
//        ) {
//            super.onCodeSent(verificationId, p1)
//            verificationID.value = verificationId
//            codeSent.value = true
//            loading.value = false
//        }
//    }
//        // Icons below the button
//        Row(
//            horizontalArrangement = Arrangement.SpaceBetween,
//            modifier = Modifier
//                .padding(16.dp)
//                .fillMaxWidth()
//        ) {
//
//        }
//        // Teal eclipse shape with text in the bottom right corner
//        Box(
//
//                    modifier = Modifier
//                    .fillMaxSize()
//
//        ) {
//
//            Image(painter = painterResource(id = R.drawable.moving), contentDescription = "Home Icon", modifier = Modifier.offset(x = 50.dp, y = 110.dp))
//
//            Image(painter = painterResource(id = R.drawable.mobile_content), contentDescription = "Home Icon", modifier = Modifier.offset(x = 253.dp, y = 178.dp))
//
//            Canvas(modifier = Modifier
//                // I posted this to show that size of Canvas doesn't matter
//                // If you don't need to use size or center params
//                .size(0.dp)
//                .offset(x = (460).dp, y = (500).dp)
//                .align(alignment = Alignment.BottomStart),
//                onDraw = {
//                    drawCircle(color = Color(0xFF55DCE3), radius = 716.dp.toPx())
//                })
//            Image(painter = painterResource(id = R.drawable.order_delivered), contentDescription = "Home Icon", modifier = Modifier.offset(x = 26.dp, y = 250.dp))
//            Image(painter = painterResource(id = R.drawable.delivery_truck), contentDescription = "Home Icon", modifier = Modifier.offset(x = 200.dp, y = 360.dp))
//            Text(
//                text = "City Go",
//                color = Color.Black,
//                modifier = Modifier
//                    .align(Alignment.BottomEnd)
//                    .padding(end = 16.dp, bottom = 16.dp)
//            )
//        }
//    }
//}




