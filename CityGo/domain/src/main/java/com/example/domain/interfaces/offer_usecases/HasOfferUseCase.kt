package com.example.domain.interfaces.offer_usecases

import com.hfad.model.OfferResponseModel

interface HasOfferUseCase {
    suspend fun execute(userRequestId: Int,serviceProviderId: String,): Int
}