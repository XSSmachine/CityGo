package com.example.domain.interfaces

import com.google.firebase.auth.FirebaseUser
import com.hfad.model.UserRequestRequestModel
import com.hfad.model.UserRequestResponseModel

interface UserRequestRepository {
    suspend fun getAllUserRequests(userId: Int): List<UserRequestResponseModel>
    suspend fun getUserRequest(userId: Int,id: Int): UserRequestResponseModel?
    suspend fun deleteUserRequest(userId: Int, id: Int)
    suspend fun updateUserRequest(userId: Int, id: Int, data: UserRequestRequestModel)
    suspend fun createUserRequest(userId:Int, data: UserRequestRequestModel)
}