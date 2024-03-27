package com.example.domain.interfaces

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthProvider
import com.hfad.model.UserProfileRequestModel
import com.hfad.model.UserProfileResponseModel
import kotlinx.coroutines.CoroutineScope

/**
 * Interface defining methods for managing user profiles and authentication.
 */
interface UserProfileRepository {
    // Retrieves the current Firebase user.


    // Creates a new user profile with the provided data.
    suspend fun createUser(name: String, surname: String, phoneNumber: String, email: String?)

    // Checks if a user with the given phone number exists.
    suspend fun userExists(phoneNumber: String): Boolean

    // Retrieves user information based on the phone number.
    suspend fun getUser(phoneNumber: String): UserProfileResponseModel?

    // Updates user information with the provided data.
    suspend fun updateUser(userId: Int, name: String, surname: String, email: String?)

    // Deletes the user account associated with the phone number.
    suspend fun deleteUser(phoneNumber: String)

    // Logs out the current user.
    fun logOut()

    // Initiates phone number-based authentication.
    suspend fun initiatePhoneNumberAuthentication(
        number: String,
        auth: FirebaseAuth,
        activity: Activity,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    )

    // Verifies the phone number with the received OTP.
    suspend fun verifyPhoneNumber(phoneNumber: String, verificationCode: String): Boolean
}
