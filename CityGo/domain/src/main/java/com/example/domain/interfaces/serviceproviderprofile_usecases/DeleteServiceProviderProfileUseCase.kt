package com.example.domain.interfaces.serviceproviderprofile_usecases

import com.hfad.model.ServiceProviderProfileRequestModel

interface DeleteServiceProviderProfileUseCase {
    suspend fun execute(cygoId : String)
}