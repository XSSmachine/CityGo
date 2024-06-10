package com.example.domain.interfaces.serviceproviderprofile_usecases

import com.hfad.model.RepoResult
import com.hfad.model.ServiceProviderProfileRequestModel
import com.hfad.model.ServiceProviderProfileResponseModel
import kotlinx.coroutines.flow.Flow

interface GetServiceProviderProfileUseCase {
    suspend fun execute(cygoId : String): RepoResult<ServiceProviderProfileResponseModel>
}