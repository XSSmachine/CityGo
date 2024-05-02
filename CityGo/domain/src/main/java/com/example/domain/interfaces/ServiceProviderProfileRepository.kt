package com.example.domain.interfaces

import com.hfad.model.ServiceProviderProfileRequestModel
import com.hfad.model.ServiceProviderProfileResponseModel


interface ServiceProviderProfileRepository {
    suspend fun getAllServiceProviders(): List<ServiceProviderProfileResponseModel>
    suspend fun getServiceProvider(cygoId: String): ServiceProviderProfileResponseModel?
    suspend fun deleteServiceProvider(cygoId: String)
    suspend fun updateServiceProvider(cygoId: String, data: ServiceProviderProfileRequestModel)
    suspend fun updateServiceProviderStatus(cygoId: String,status:String)
    suspend fun createServiceProvider( data: ServiceProviderProfileRequestModel)
}