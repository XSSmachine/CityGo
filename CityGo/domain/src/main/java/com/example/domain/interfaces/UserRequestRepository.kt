package com.example.domain.interfaces

import com.google.firebase.auth.FirebaseUser
import com.hfad.model.UserRequestRequestModel
import com.hfad.model.UserRequestResponseModel

interface UserRequestRepository {
    suspend fun getAllUserRequests(userId: String): List<UserRequestResponseModel>
    suspend fun getAllUserRequestsForCurrentUser(userId: String): List<UserRequestResponseModel>
    suspend fun getUserRequest(userId: String,id: Int): UserRequestResponseModel?
    suspend fun getUserRequestById(id: Int): UserRequestResponseModel?
    suspend fun deleteUserRequest(userId: String, id: Int)
    suspend fun updateUserRequest(userId: String, id: Int, data: UserRequestRequestModel)
    suspend fun createUserRequest( data: UserRequestRequestModel)
}