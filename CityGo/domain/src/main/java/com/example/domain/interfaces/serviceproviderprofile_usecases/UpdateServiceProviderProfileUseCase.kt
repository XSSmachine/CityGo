package com.example.domain.interfaces.serviceproviderprofile_usecases

import com.hfad.model.ServiceProviderProfileRequestModel

interface UpdateServiceProviderProfileUseCase {
    suspend fun execute(cygoId:String, data : ServiceProviderProfileRequestModel)
}