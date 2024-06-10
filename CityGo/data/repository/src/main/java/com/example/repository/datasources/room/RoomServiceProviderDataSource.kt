package com.example.repository.datasources.room

import com.example.repository.datasources.room.entities.ServiceProviderProfileRoomEntity
import com.example.repository.datasources.room.entities.toServiceProviderProfileResponseModel
import com.example.repository.datasources.room.entities.toServiceProviderProfileRoomEntity
import com.example.repository.datasources.room.entities.toUserRequestResponseModel
import com.example.repository.datasources.room.entities.toUserRequestRoomEntity
import com.example.repository.interfaces.ServiceProviderDao
import com.example.repository.interfaces.ServiceProvidersDataSource
import com.hfad.model.BasicError
import com.hfad.model.ErrorCode
import com.hfad.model.Failure
import com.hfad.model.RepoResult
import com.hfad.model.ServiceProviderProfileRequestModel
import com.hfad.model.ServiceProviderProfileResponseModel
import com.hfad.model.Success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

class RoomServiceProviderDataSource constructor(private val dao:ServiceProviderDao)
    : ServiceProvidersDataSource{


    override suspend fun create(worker: ServiceProviderProfileRequestModel): RepoResult<Unit> {

        return try {
            dao.createServiceProvider(worker.toServiceProviderProfileRoomEntity())
            Success(Unit) // Return Success with Unit since there's no body in the response
        } catch (e: Exception) {
            Failure(BasicError(e, ErrorCode.ERROR))
        }
    }

    override suspend fun update(id: String, worker: ServiceProviderProfileRequestModel): RepoResult<Unit> {
        return try {
            dao.updateServiceProvider(id,worker.name,worker.surname,worker.email,
                worker.dateOfBirth,worker.address,worker.latitude,
                worker.longitude,worker.zipCode,worker.city,worker.country,
                worker.phoneNumber,worker.profilePicture,worker.idPictureFront,worker.idPictureBack,
                worker.vehiclePicture,worker.stars,worker.status)
            Success(Unit) // Return Success with Unit since there's no body in the response
        } catch (e: Exception) {
            Failure(BasicError(e, ErrorCode.ERROR))
        }
    }

    override suspend fun updateStatus(id: String, status: String): RepoResult<Unit> {
        return try {
            dao.updateStatus(id, status)
            Success(Unit) // Return Success with Unit since there's no body in the response
        } catch (e: Exception) {
            Failure(BasicError(e, ErrorCode.ERROR))
        }
    }

    override suspend fun delete(userId: String): RepoResult<Unit> {
        return try {
            dao.deleteServiceProvider(userId)
            Success(Unit) // Return Success with Unit since there's no body in the response
        } catch (e: Exception) {
            Failure(BasicError(e, ErrorCode.ERROR))
        }
    }

    override suspend fun getById(cygoId: String): RepoResult<ServiceProviderProfileResponseModel> = try{
        val worker = dao.getServiceProviderById(cygoId)
        Success(worker.toServiceProviderProfileResponseModel())
    }catch (e: Exception){
        Failure(BasicError(e))
    }

    override suspend fun getAll(): RepoResult<List<ServiceProviderProfileResponseModel>> = try {
        val workers = dao.getAllServiceProviders().toList().map {
            it.toServiceProviderProfileResponseModel()
        }
        Success(workers)
    }catch (e: Exception){
        Failure(BasicError(e))
    }
}