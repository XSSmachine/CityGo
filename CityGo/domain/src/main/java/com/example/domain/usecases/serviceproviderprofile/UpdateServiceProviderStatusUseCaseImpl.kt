package com.example.domain.usecases.serviceproviderprofile

import com.example.domain.interfaces.ServiceProviderProfileRepository
import com.example.domain.interfaces.serviceproviderprofile_usecases.UpdateServiceProviderStatusUseCase

class UpdateServiceProviderStatusUseCaseImpl constructor(private val serviceProviderProfileRepository: ServiceProviderProfileRepository): UpdateServiceProviderStatusUseCase {
    override suspend fun execute(cygoId: String, status: String) {
        serviceProviderProfileRepository.updateServiceProviderStatus(cygoId,status)
    }
}