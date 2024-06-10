package com.example.network.interfaces

import com.hfad.model.RepoResult
import com.hfad.model.ResponseBody
import com.hfad.model.UserRequestRequestModel
import com.hfad.model.UserRequestResponseModel

interface UserRequestRemoteDataSource {
    suspend fun getAll(): RepoResult<List<UserRequestResponseModel>>

//    suspend fun getAllForCurrentUser(userId: String): RepoResult<List<UserRequestResponseModel>>

    suspend fun getOne(sid:String): RepoResult<UserRequestResponseModel>

    suspend fun delete(sid: String):RepoResult<Unit>

    suspend fun update(sid: String, contact: UserRequestRequestModel):RepoResult<Unit>

    suspend fun create(data: UserRequestRequestModel):RepoResult<ResponseBody>

    suspend fun updateOffers(userId: String, offerId: String): RepoResult<Unit>

    suspend fun deleteOffers(userId: String, offerId: String): RepoResult<Unit>
}