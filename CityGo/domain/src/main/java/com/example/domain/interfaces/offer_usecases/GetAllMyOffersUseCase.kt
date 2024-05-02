package com.example.domain.interfaces.offer_usecases

import com.hfad.model.OfferResponseModel

interface GetAllMyOffersUseCase {
    suspend fun execute(serviceProviderId: String): List<OfferResponseModel>
}