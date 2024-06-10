package com.example.repository.interfaces

import androidx.room.Query
import com.example.repository.datasources.room.entities.ServiceProviderProfileRoomEntity
import com.hfad.model.RepoResult
import com.hfad.model.ServiceProviderProfileRequestModel
import com.hfad.model.ServiceProviderProfileResponseModel
import com.hfad.model.UserRequestResponseModel
import kotlinx.coroutines.flow.Flow

interface NullableSPModel{
    val model : ServiceProviderProfileResponseModel?
}
interface ServiceProvidersDataSource {

    suspend fun create(worker: ServiceProviderProfileRequestModel):RepoResult<Unit>


    suspend fun update(id: String, worker: ServiceProviderProfileRequestModel):RepoResult<Unit>

    suspend fun updateStatus(id: String,status:String):RepoResult<Unit>


    suspend fun delete(cygoId: String):RepoResult<Unit>


    suspend fun getById(cygoId: String): RepoResult<ServiceProviderProfileResponseModel>


    suspend fun getAll(): RepoResult<List<ServiceProviderProfileResponseModel>>
}