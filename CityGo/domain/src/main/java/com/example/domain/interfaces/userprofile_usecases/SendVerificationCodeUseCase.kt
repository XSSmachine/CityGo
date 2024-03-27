package com.example.domain.interfaces.userprofile_usecases

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider

interface SendVerificationCodeUseCase {
    suspend fun execute(number: String,
                        auth: FirebaseAuth,
                        activity: Activity,
                        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks)
}