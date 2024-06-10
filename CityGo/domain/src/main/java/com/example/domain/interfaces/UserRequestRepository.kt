package com.example.domain.interfaces

import com.hfad.model.RepoResult
import com.hfad.model.ServiceProviderProfileResponseModel
import com.hfad.model.UserRequestRequestModel
import com.hfad.model.UserRequestResponseModel

interface NullableUserRequestModel{
    val model : UserRequestResponseModel?
}

interface UserRequestRepository {
    suspend fun getAllUserRequests(): RepoResult<List<UserRequestResponseModel>>
    suspend fun getAllUserRequestsForCurrentUser(userId: String): RepoResult<List<UserRequestResponseModel>>
    suspend fun getUserRequest(userId: String,uuid: String): RepoResult<UserRequestResponseModel>
    suspend fun getRemoteUserRequest(sid:String):RepoResult<UserRequestResponseModel>
    suspend fun getUserRequestById(uuid: String): RepoResult<UserRequestResponseModel>
    suspend fun deleteUserRequest(userId: String, uuid: String):RepoResult<Unit>
    suspend fun updateUserRequest(userId: String, uuid: String, data: UserRequestRequestModel):RepoResult<Unit>
    suspend fun createUserRequest( data: UserRequestRequestModel):RepoResult<Unit>
}