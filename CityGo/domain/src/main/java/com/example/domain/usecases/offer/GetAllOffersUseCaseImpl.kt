package com.example.domain.usecases.offer

import com.example.domain.interfaces.OfferRepository
import com.example.domain.interfaces.offer_usecases.GetAllOffersUseCase
import com.hfad.model.OfferResponseModel

class GetAllOffersUseCaseImpl constructor(private val offerRepository: OfferRepository
):GetAllOffersUseCase {
    override suspend fun execute(userRequestId:Int): List<OfferResponseModel> {
        return offerRepository.getAllOffers(userRequestId)
    }
}