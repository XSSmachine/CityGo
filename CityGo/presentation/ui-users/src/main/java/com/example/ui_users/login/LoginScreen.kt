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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
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
) {

    @Composable
    fun Int.responsiveWidth(): Dp {
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.dp
        return (this * screenWidth.value / 100).dp
    }

    @Composable
    fun Int.responsiveHeight(): Dp {
        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp
        return (this * screenHeight.value / 100).dp
    }

    var uid = rememberSaveable {
        mutableStateOf("")
    }
    val otp = rememberSaveable {
        mutableStateOf("")
    }

    val verificationID = rememberSaveable {
        mutableStateOf("")
    }

    val codeSent = rememberSaveable {
        mutableStateOf(false)
    }

    val loading = rememberSaveable {
        mutableStateOf(false)
    }//Optional

    val context = LocalContext.current

    val mAuth: FirebaseAuth = Firebase.auth

    lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE1E355))
    ) {
        Row(modifier = Modifier.padding(3.responsiveHeight())) {
            Text(
                text = "CYGO",
                fontWeight = FontWeight.Bold,
                fontSize = 64.sp,
                modifier = Modifier
                    .padding(start = 2.responsiveWidth())
                    .align(Alignment.Top)

            )
            Spacer(modifier = Modifier.width(3.responsiveWidth()))
            Image(
                painter = painterResource(id = R.drawable.relax),
                contentDescription = "Home Icon",
                modifier = Modifier.padding(top = 2.responsiveHeight())
            )
        }
        Spacer(modifier = Modifier.height(4.responsiveHeight()))
        // Input field with icon prefix
        TextField(
            enabled = !codeSent.value && !loading.value,
            value = createUserProfileViewModel.phoneNumber,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = { if (it.length <= 9) createUserProfileViewModel.setPhoneNumber(it) },
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.hr),
                    contentDescription = "Phone Icon"
                )
            },
            placeholder = { Text(stringResource(id = R.string.phone_num)) },
            prefix = { Text(text = " +385  ", color = Color.Black) },
            modifier = Modifier
                .padding(1.responsiveHeight())
                .fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.DarkGray,
                disabledTextColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Gray,
                unfocusedIndicatorColor = Color.Black,
                disabledIndicatorColor = Color.Transparent
            )
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
            val validPhone = stringResource(id = R.string.valid_phone)
            Button(
                enabled = !loading.value && !codeSent.value,
                onClick = {
                    if (TextUtils.isEmpty(createUserProfileViewModel.phoneNumber) || createUserProfileViewModel.phoneNumber.length < 9) {
                        Toast.makeText(
                            context,
                            validPhone,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else {
                        loading.value = true
                        val number = "+3850${createUserProfileViewModel.phoneNumber}"
                        createUserProfileViewModel.sendVerificationCode(
                            number,
                            mAuth,
                            context as Activity,
                            callbacks
                        )
                    }
                },
                modifier = Modifier
                    .padding(1.responsiveHeight())
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.LightGray
                )
            ) {
                Text(stringResource(id = R.string.send_code), color = Color.Black)
            }
        }

        AnimatedVisibility(
            visible = codeSent.value,
            // Add same animation here
        ) {
            Column(modifier = Modifier.wrapContentHeight()) {
                TextField(
                    enabled = !loading.value,
                    value = otp.value,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onValueChange = { if (it.length <= 6) otp.value = it },
                    placeholder = { Text(text = stringResource(id = R.string.enter_otp)) },
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
                        disabledIndicatorColor = Color.Transparent
                    ),
                    supportingText = {
                        Text(
                            text = "${otp.value.length} / 6",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End,
                        )
                    }
                )
                val fail = stringResource(id = R.string.verification_fail);
                val valid = stringResource(id = R.string.valid_otp)
                Button(
                    enabled = !loading.value,
                    onClick = {
                        if (TextUtils.isEmpty(otp.value) || otp.value.length < 6) {
                            Toast.makeText(
                                context,
                                valid,
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

                                            Log.d(
                                                "PHONE",
                                                createUserProfileViewModel.checkIfUserExists(
                                                    createUserProfileViewModel.phoneNumber
                                                ).toString()
                                            )
                                            if (createUserProfileViewModel.checkIfUserExists(
                                                    createUserProfileViewModel.phoneNumber
                                                ) == true
                                            ) {
                                                navController.navigate("home")
                                                if (user != null) {
                                                    Log.d("LOGIN", user.uid)
//                                                    createUserProfileViewModel.cacheName(user.uid)
                                                    createUserProfileViewModel.setUserId(user.uid)
                                                }
                                            } else {
                                                navController.navigate("dialog")
                                            }
                                        }
                                    } else {
                                        loading.value = false
                                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                                            Toast.makeText(
                                                context,
                                                fail + (task.exception as FirebaseAuthInvalidCredentialsException).message,
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
                        .padding(1.responsiveHeight())
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.LightGray
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.verify_text),
                        modifier = Modifier.padding(1.responsiveHeight())
                    )
                }
            }
        }
    }

    if (loading.value) {
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
    }
    val success = stringResource(id = R.string.verification_success);
    callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            Toast.makeText(context, success, Toast.LENGTH_SHORT).show()
            loading.value = false
        }

        val fail = stringResource(id = R.string.verification_fail);
        override fun onVerificationFailed(p0: FirebaseException) {
            Toast.makeText(context, fail + " ${p0.message}", Toast.LENGTH_LONG)
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
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(2.responsiveHeight())
            .fillMaxWidth()
    ) {

    }
    Box(

        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.responsiveHeight())

    ) {

        val radius = 87.responsiveHeight();
        Image(
            painter = painterResource(id = R.drawable.moving),
            contentDescription = "Home Icon",
            modifier = Modifier.offset(x = 7.responsiveWidth(), y = 14.responsiveHeight())
        )

        Image(
            painter = painterResource(id = R.drawable.mobile_content),
            contentDescription = "Home Icon",
            modifier = Modifier.offset(x = 62.responsiveWidth(), y = 21.responsiveHeight())
        )

        Canvas(modifier = Modifier
            .size(0.dp)
            .offset(x = (100.responsiveWidth()), y = 62.responsiveHeight())
            .align(alignment = Alignment.BottomStart),
            onDraw = {
                drawCircle(color = Color(0xFF55DCE3), radius = radius.toPx())
            })
        Image(
            painter = painterResource(id = R.drawable.order_delivered),
            contentDescription = "Home Icon",
            modifier = Modifier.offset(x = 7.responsiveWidth(), y = 31.responsiveHeight())
        )
        Image(
            painter = painterResource(id = R.drawable.delivery_truck),
            contentDescription = "Home Icon",
            modifier = Modifier.offset(x = 48.responsiveWidth(), y = 44.responsiveHeight())
        )
        Text(
            text = "City Go",
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 2.responsiveHeight(), bottom = 2.responsiveWidth())
        )
    }
}




