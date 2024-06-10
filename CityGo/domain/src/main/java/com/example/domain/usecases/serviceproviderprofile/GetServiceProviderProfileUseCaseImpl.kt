package com.example.domain.usecases.serviceproviderprofile

import com.example.domain.interfaces.ServiceProviderProfileRepository
import com.example.domain.interfaces.serviceproviderprofile_usecases.GetServiceProviderProfileUseCase
import com.hfad.model.BasicError
import com.hfad.model.Failure
import com.hfad.model.RepoResult
import com.hfad.model.ServiceProviderProfileResponseModel
import com.hfad.model.Success
import com.hfad.model.onFailure
import com.hfad.model.onSuccess
import kotlinx.coroutines.flow.Flow

class GetServiceProviderProfileUseCaseImpl constructor(private val serviceProviderProfileRepository: ServiceProviderProfileRepository
):GetServiceProviderProfileUseCase{
    override suspend fun execute(cygoId: String): RepoResult<ServiceProviderProfileResponseModel> {
        try {
            serviceProviderProfileRepository.getServiceProvider(cygoId)
            .onSuccess { return Success(it) }
                .onFailure { return Failure(it) }
        }catch (e: Exception) {
            e.printStackTrace()
            Failure(BasicError(Throwable("Get All Service Providers Fetch failed")))
        }
        return Failure(BasicError(Throwable("Get All Service Providers Fetch failed")))
    }
}