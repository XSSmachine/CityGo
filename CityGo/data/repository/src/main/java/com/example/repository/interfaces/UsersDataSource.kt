package com.example.repository.interfaces

import android.app.Activity
import com.example.common.ScreenViewState
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.hfad.model.UserProfileRequestModel
import com.hfad.model.UserProfileResponseModel
import kotlinx.coroutines.flow.Flow


interface UsersDataSource {

    // Create a new user account using phone number authentication
    suspend fun createUserWithPhoneNumber(phoneNumber: String, name: String, surname: String, email: String?,stars:Double)

    // Check if a user with the given phone number exists
    suspend fun userExists(phoneNumber: String): Boolean

    // Get user information by phone number
    suspend fun getUserByPhoneNumber(phoneNumber: String): UserProfileResponseModel?

    // Update user information
    suspend fun updateUser(userId: Int, data: UserProfileRequestModel)

    // Delete user account
    suspend fun deleteUser(userId: Int)

//    // Send verification code to user's phone number
//    suspend fun sendVerificationCode(activity: Activity,phoneNumber: String)
//
//    // Verify user's phone number with the received verification code
//    suspend fun verifyPhoneNumber(phoneNumber: String, verificationCode: String): Boolean
}

