package com.example.domain.usecases.userprofile

import android.app.Activity
import com.example.domain.interfaces.UserProfileRepository
import com.example.domain.interfaces.userprofile_usecases.GetUserProfileUseCase
import com.example.domain.interfaces.userprofile_usecases.SendVerificationCodeUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider

class SendVerificationCodeUseCaseImpl constructor(private val userProfileRepository: UserProfileRepository) :
    SendVerificationCodeUseCase {
    override suspend fun execute(
        number: String,
        auth: FirebaseAuth,
        activity: Activity,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    ) {
        return userProfileRepository.initiatePhoneNumberAuthentication(number,auth,activity,callbacks)
    }
}