package com.example.network.interfaces

import com.hfad.model.RepoResult
import com.hfad.model.ResponseBody
import com.hfad.model.ServiceProviderProfileRequestModel
import com.hfad.model.ServiceProviderProfileResponseModel

interface ProviderRemoteDataSource {
    suspend fun create(providerId:String,worker: ServiceProviderProfileRequestModel):RepoResult<ServiceProviderProfileResponseModel>


    suspend fun update(id: String, worker: ServiceProviderProfileRequestModel):RepoResult<Unit>


    suspend fun updateStatus(id: String, status: Map<String,String>):RepoResult<Unit>


    suspend fun delete(providerId: String):RepoResult<Unit>


    suspend fun getById(providerId: String): RepoResult<ServiceProviderProfileResponseModel>

    suspend fun getAll(): RepoResult<List<ServiceProviderProfileResponseModel>>

    suspend fun updateOffers(userId: String, offerId: String): RepoResult<Unit>

    suspend fun deleteOffers(userId: String, offerId: String): RepoResult<Unit>
}