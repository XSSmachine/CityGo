package com.example.repository.datasources.room

import com.example.repository.datasources.room.entities.ServiceProviderProfileRoomEntity
import com.example.repository.datasources.room.entities.toServiceProviderProfileResponseModel
import com.example.repository.datasources.room.entities.toServiceProviderProfileRoomEntity
import com.example.repository.datasources.room.entities.toUserRequestResponseModel
import com.example.repository.datasources.room.entities.toUserRequestRoomEntity
import com.example.repository.interfaces.ServiceProviderDao
import com.example.repository.interfaces.ServiceProvidersDataSource
import com.hfad.model.ServiceProviderProfileRequestModel
import com.hfad.model.ServiceProviderProfileResponseModel

class RoomServiceProviderDataSource constructor(private val dao:ServiceProviderDao)
    : ServiceProvidersDataSource{


    override suspend fun create(worker: ServiceProviderProfileRequestModel) {
        dao.createServiceProvider(worker.toServiceProviderProfileRoomEntity())
    }

    override suspend fun update(id: String, worker: ServiceProviderProfileRequestModel) {
        dao.updateServiceProvider(id,worker.name,worker.surname,worker.email,
            worker.dateOfBirth,worker.address,worker.latitude,
            worker.longitude,worker.zipCode,worker.city,worker.country,
            worker.phoneNumber,worker.profilePicture,worker.idPictureFront,worker.idPictureBack,
            worker.vehiclePicture,worker.stars,worker.status)
    }

    override suspend fun updateStatus(id: String, status: String) {
        dao.updateStatus(id, status)
    }

    override suspend fun delete(userId: String) {
        dao.deleteServiceProvider(userId)
    }

    override suspend fun getById(cygoId: String): ServiceProviderProfileResponseModel? {
        val worker = dao.getServiceProviderById(cygoId)
        return worker?.toServiceProviderProfileResponseModel()
    }

    override suspend fun getAll(): List<ServiceProviderProfileResponseModel> {
        return dao.getAllServiceProviders().toList().map {
            it.toServiceProviderProfileResponseModel()
        }
    }
}