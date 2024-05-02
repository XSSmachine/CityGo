package com.example.domain.interfaces.offer_usecases

interface UpdateOfferStatusUseCase {
    suspend fun execute( userRequestId: Int,serviceProviderId: String, status: String)
}