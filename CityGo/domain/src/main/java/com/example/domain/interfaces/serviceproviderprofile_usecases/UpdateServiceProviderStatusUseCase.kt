package com.example.domain.interfaces.serviceproviderprofile_usecases

import com.hfad.model.ServiceProviderProfileRequestModel

interface UpdateServiceProviderStatusUseCase {
    suspend fun execute(cygoId:String, status: String)
}