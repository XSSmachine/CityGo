package com.example.repository.interfaces

import androidx.room.Query
import com.example.repository.datasources.room.entities.ServiceProviderProfileRoomEntity
import com.hfad.model.ServiceProviderProfileRequestModel
import com.hfad.model.ServiceProviderProfileResponseModel
import com.hfad.model.UserRequestResponseModel

interface ServiceProvidersDataSource {

    suspend fun create(worker: ServiceProviderProfileRequestModel)


    suspend fun update(id: String, worker: ServiceProviderProfileRequestModel)

    suspend fun updateStatus(id: String,status:String)


    suspend fun delete(cygoId: String)


    suspend fun getById(cygoId: String): ServiceProviderProfileResponseModel?


    suspend fun getAll(): List<ServiceProviderProfileResponseModel>
}