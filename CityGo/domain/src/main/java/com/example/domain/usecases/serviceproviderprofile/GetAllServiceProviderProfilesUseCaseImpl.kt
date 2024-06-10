package com.example.domain.usecases.serviceproviderprofile

import com.example.domain.interfaces.ServiceProviderProfileRepository
import com.example.domain.interfaces.serviceproviderprofile_usecases.GetAllServiceProviderProfilesUseCase
import com.hfad.model.BasicError
import com.hfad.model.Failure
import com.hfad.model.RepoResult
import com.hfad.model.ServiceProviderProfileResponseModel
import com.hfad.model.Success
import com.hfad.model.onFailure
import com.hfad.model.onSuccess
import kotlinx.coroutines.flow.Flow

class GetAllServiceProviderProfilesUseCaseImpl constructor(private val serviceProviderProfileRepository: ServiceProviderProfileRepository
):GetAllServiceProviderProfilesUseCase{
    override suspend fun execute(): RepoResult<List<ServiceProviderProfileResponseModel>> {
        try {
            serviceProviderProfileRepository.getAllServiceProviders()
            .onSuccess { return Success(it) }
            .onFailure { return Failure(it) }
        }catch (e: Exception) {
            e.printStackTrace()
            Failure(BasicError(Throwable("Get All Service Providers Fetch failed")))
        }
        return Failure(BasicError(Throwable("Get All Service Providers Fetch failed")))
    }
}