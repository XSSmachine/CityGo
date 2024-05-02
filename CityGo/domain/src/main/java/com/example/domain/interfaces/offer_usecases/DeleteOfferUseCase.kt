package com.example.domain.interfaces.offer_usecases

interface DeleteOfferUseCase {
    suspend fun execute(userRequestId: Int,serviceProviderId: String,)
}