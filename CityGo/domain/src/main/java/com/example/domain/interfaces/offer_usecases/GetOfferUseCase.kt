package com.example.domain.interfaces.offer_usecases

import com.hfad.model.OfferResponseModel

interface GetOfferUseCase {
    suspend fun execute(userRequestId: Int,serviceProviderId: String): OfferResponseModel?
}