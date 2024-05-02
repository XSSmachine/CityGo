package com.example.domain.usecases.serviceproviderprofile

import com.example.domain.interfaces.ServiceProviderProfileRepository
import com.example.domain.interfaces.serviceproviderprofile_usecases.UpdateServiceProviderProfileUseCase
import com.hfad.model.ServiceProviderProfileRequestModel

class UpdateServiceProviderProfileUseCaseImpl constructor(private val serviceProviderProfileRepository: ServiceProviderProfileRepository
):UpdateServiceProviderProfileUseCase{
    override suspend fun execute(cygoId: String, data: ServiceProviderProfileRequestModel) {
        serviceProviderProfileRepository.updateServiceProvider(cygoId,data)
    }
}