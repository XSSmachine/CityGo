package com.example.domain.usecases.serviceproviderprofile

import com.example.domain.interfaces.ServiceProviderProfileRepository
import com.example.domain.interfaces.serviceproviderprofile_usecases.GetAllServiceProviderProfilesUseCase
import com.hfad.model.ServiceProviderProfileResponseModel

class GetAllServiceProviderProfilesUseCaseImpl constructor(private val serviceProviderProfileRepository: ServiceProviderProfileRepository
):GetAllServiceProviderProfilesUseCase{
    override suspend fun execute(): List<ServiceProviderProfileResponseModel> {
        return serviceProviderProfileRepository.getAllServiceProviders()
    }
}