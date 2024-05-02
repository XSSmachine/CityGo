package com.example.domain.interfaces.serviceproviderprofile_usecases

import com.hfad.model.ServiceProviderProfileRequestModel
import com.hfad.model.ServiceProviderProfileResponseModel

interface GetAllServiceProviderProfilesUseCase {
    suspend fun execute():List<ServiceProviderProfileResponseModel>
}