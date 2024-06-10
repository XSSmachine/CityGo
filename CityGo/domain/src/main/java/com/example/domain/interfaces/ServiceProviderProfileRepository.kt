package com.example.domain.interfaces

import com.hfad.model.RepoResult
import com.hfad.model.ServiceProviderProfileRequestModel
import com.hfad.model.ServiceProviderProfileResponseModel


interface NullableSPModel{
    val model : ServiceProviderProfileResponseModel?
}
interface ServiceProviderProfileRepository {
    suspend fun getAllServiceProviders(): RepoResult<List<ServiceProviderProfileResponseModel>>
    suspend fun getServiceProvider(cygoId: String): RepoResult<ServiceProviderProfileResponseModel>
    suspend fun deleteServiceProvider(cygoId: String):RepoResult<Unit>
    suspend fun updateServiceProvider(cygoId: String, data: ServiceProviderProfileRequestModel):RepoResult<Unit>
    suspend fun updateServiceProviderStatus(cygoId: String,status:String):RepoResult<Unit>
    suspend fun createServiceProvider( data: ServiceProviderProfileRequestModel):RepoResult<Unit>
}