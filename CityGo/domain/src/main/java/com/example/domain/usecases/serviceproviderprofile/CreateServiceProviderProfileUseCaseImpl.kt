package com.example.domain.usecases.serviceproviderprofile

import com.example.domain.interfaces.ServiceProviderProfileRepository
import com.example.domain.interfaces.serviceproviderprofile_usecases.CreateServiceProviderProfileUseCase
import com.hfad.model.ServiceProviderProfileRequestModel

class CreateServiceProviderProfileUseCaseImpl constructor(private val serviceProviderProfileRepository: ServiceProviderProfileRepository
):CreateServiceProviderProfileUseCase {
    override suspend fun execute(data: ServiceProviderProfileRequestModel) {
        serviceProviderProfileRepository.createServiceProvider(data)
    }

}