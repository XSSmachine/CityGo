package com.example.repository.interfaces

import com.hfad.model.RepoResult
import com.hfad.model.UserProfileRequestModel
import com.hfad.model.UserProfileResponseModel
import com.hfad.model.UserRequestResponseModel
import kotlinx.coroutines.flow.Flow


interface NullableUserModel{
    val model : UserRequestResponseModel?
}


interface UsersDataSource {

    // Create a new user account using phone number authentication
    suspend fun createUserWithPhoneNumber(userId: String,phoneNumber: String, name: String, surname: String, email: String?,stars:Double,sid:String?):RepoResult<Unit>

    // Check if a user with the given phone number exists
    suspend fun userExists(phoneNumber: String): RepoResult<Boolean>

    // Get user information by phone number
    suspend fun getUserById(userId: String): RepoResult<UserProfileResponseModel>

    // Update user information
    suspend fun updateUser(userId: String, data: UserProfileRequestModel):RepoResult<Unit>

    // Delete user account
    suspend fun deleteUser(userId: String):RepoResult<Unit>

//    // Send verification code to user's phone number
//    suspend fun sendVerificationCode(activity: Activity,phoneNumber: String)
//
//    // Verify user's phone number with the received verification code
//    suspend fun verifyPhoneNumber(phoneNumber: String, verificationCode: String): Boolean
}

