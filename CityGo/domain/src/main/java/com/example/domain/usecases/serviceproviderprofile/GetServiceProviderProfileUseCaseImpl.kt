package com.example.domain.usecases.serviceproviderprofile

import com.example.domain.interfaces.ServiceProviderProfileRepository
import com.example.domain.interfaces.serviceproviderprofile_usecases.GetServiceProviderProfileUseCase
import com.hfad.model.ServiceProviderProfileResponseModel

class GetServiceProviderProfileUseCaseImpl constructor(private val serviceProviderProfileRepository: ServiceProviderProfileRepository
):GetServiceProviderProfileUseCase{
    override suspend fun execute(cygoId: String): ServiceProviderProfileResponseModel? {
        return serviceProviderProfileRepository.getServiceProvider(cygoId)
    }
}