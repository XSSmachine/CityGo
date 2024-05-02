package com.example.domain.usecases.offer

import com.example.domain.interfaces.OfferRepository
import com.example.domain.interfaces.offer_usecases.CreateOfferUseCase
import com.hfad.model.OfferRequestModel

class CreateOfferUseCaseImpl constructor( private val offerRepository: OfferRepository):CreateOfferUseCase {
    override suspend fun execute(offer: OfferRequestModel) {
        offerRepository.createOffer(offer)
    }
}