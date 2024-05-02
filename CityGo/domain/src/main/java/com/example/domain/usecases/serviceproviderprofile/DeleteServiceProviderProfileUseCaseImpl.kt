package com.example.domain.usecases.serviceproviderprofile

import com.example.domain.interfaces.ServiceProviderProfileRepository
import com.example.domain.interfaces.serviceproviderprofile_usecases.DeleteServiceProviderProfileUseCase

class DeleteServiceProviderProfileUseCaseImpl constructor(private val serviceProviderProfileRepository: ServiceProviderProfileRepository
):DeleteServiceProviderProfileUseCase{
    override suspend fun execute(cygoId: String) {
        serviceProviderProfileRepository.deleteServiceProvider(cygoId)
    }
}