package com.example.domain.interfaces.serviceproviderprofile_usecases

import com.hfad.model.ServiceProviderProfileRequestModel

interface CreateServiceProviderProfileUseCase {
    suspend fun execute(data : ServiceProviderProfileRequestModel)
}