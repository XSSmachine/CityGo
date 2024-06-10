package com.example.domain.interfaces

import android.app.Activity
import com.hfad.model.RepoResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.hfad.model.ServiceProviderProfileResponseModel
import com.hfad.model.UserProfileResponseModel


interface NullableUserModel{
    val model : ServiceProviderProfileResponseModel?
}

/**
 * Interface defining methods for managing user profiles and authentication.
 */
interface UserProfileRepository {
    // Retrieves the current Firebase user.


    // Creates a new user profile with the provided data.
    suspend fun createUser(userId:String,name: String, surname: String, phoneNumber: String, email: String?):RepoResult<Unit>

    // Checks if a user with the given phone number exists.
    suspend fun userExists(phoneNumber: String): RepoResult<Boolean>

    // Retrieves user information based on the phone number.
    suspend fun getUser(UserId: String): RepoResult<UserProfileResponseModel>

    // Updates user information with the provided data.
    suspend fun updateUser(userId: String, name: String, surname: String, email: String,photo:String?):RepoResult<Unit>

    // Deletes the user account associated with the phone number.
    suspend fun deleteUser(phoneNumber: String):RepoResult<Unit>

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
