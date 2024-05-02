package com.example.domain.repositories

import com.example.domain.interfaces.ServiceProviderProfileRepository
import com.example.repository.interfaces.ServiceProvidersDataSource
import com.hfad.model.ServiceProviderProfileRequestModel
import com.hfad.model.ServiceProviderProfileResponseModel

class ServiceProviderProfileRepositoryImpl constructor(private val serviceProvidersDataSource: ServiceProvidersDataSource
):ServiceProviderProfileRepository {
    override suspend fun getAllServiceProviders(): List<ServiceProviderProfileResponseModel> {
        return serviceProvidersDataSource.getAll()
    }

    override suspend fun getServiceProvider(cygoId: String): ServiceProviderProfileResponseModel? {
        return serviceProvidersDataSource.getById(cygoId)
    }

    override suspend fun deleteServiceProvider(cygoId: String) {
        serviceProvidersDataSource.delete(cygoId)
    }

    override suspend fun updateServiceProvider(
        cygoId: String,
        data: ServiceProviderProfileRequestModel
    ) {
        serviceProvidersDataSource.update(cygoId,data)
    }

    override suspend fun updateServiceProviderStatus(cygoId: String, status: String) {
        serviceProvidersDataSource.updateStatus(cygoId,status)
    }

    override suspend fun createServiceProvider(data: ServiceProviderProfileRequestModel) {
        serviceProvidersDataSource.create(data)
    }
}