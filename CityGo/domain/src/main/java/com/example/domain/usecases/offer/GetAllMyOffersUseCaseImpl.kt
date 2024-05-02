package com.example.domain.usecases.offer

import com.example.domain.interfaces.OfferRepository
import com.example.domain.interfaces.offer_usecases.GetAllMyOffersUseCase
import com.example.domain.interfaces.offer_usecases.GetAllOffersUseCase
import com.hfad.model.OfferResponseModel

class GetAllMyOffersUseCaseImpl constructor(private val offerRepository: OfferRepository
): GetAllMyOffersUseCase {
    override suspend fun execute(serviceProviderId: String): List<OfferResponseModel> {
        return offerRepository.getAllMyOffers(serviceProviderId)
    }
}