package com.example.network.interfaces

import com.hfad.model.RepoResult
import com.hfad.model.ResponseBody
import com.hfad.model.UserProfileRequestModel
import com.hfad.model.UserProfileResponseModel

interface UserRemoteDataSource {
    suspend fun createUser(userId: String, data: UserProfileRequestModel):RepoResult<UserProfileResponseModel>

    // Check if a user with the given phone number exists
   suspend fun userExists(phoneNumber: String): RepoResult<Boolean>

    // Get user information by phone number
    suspend fun getUserById(userId: String): RepoResult<UserProfileResponseModel>

    // Update user information
    suspend fun updateUser(userId: String, data: UserProfileRequestModel): RepoResult<Unit>

    suspend fun updateUserRequests(userId: String, requestId: String): RepoResult<Unit>

    // Delete user account
    suspend fun deleteUser(userId: String):RepoResult<Unit>

    suspend fun deleteUserRequests(userId:String, requestId:String):RepoResult<Unit>
}