package com.example.repository.interfaces

import com.hfad.model.RepoResult
import com.hfad.model.ServiceProviderProfileResponseModel
import com.hfad.model.UserRequestRequestModel
import com.hfad.model.UserRequestResponseModel
import kotlinx.coroutines.flow.Flow


interface NullableUserRequestModel{
    val model : UserRequestResponseModel?
}

interface UserRequestDataSource {
    suspend fun getAll(): RepoResult<List<UserRequestResponseModel>>

    suspend fun getAllForCurrentUser(userId: String): RepoResult<List<UserRequestResponseModel>>

    suspend fun getOne(userId: String, uuid: String): RepoResult<UserRequestResponseModel>
    suspend fun getOneById( uuid: String): RepoResult<UserRequestResponseModel>

    suspend fun delete(userId: String, uuid: String):RepoResult<Unit>

    suspend fun update(userId: String, uuid: String, data: UserRequestRequestModel):RepoResult<Unit>

    suspend fun create(data: UserRequestRequestModel) : RepoResult<Unit>
}