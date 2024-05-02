package com.example.domain.usecases.offer

import com.example.domain.interfaces.OfferRepository
import com.example.domain.interfaces.offer_usecases.UpdateOfferUseCase
import com.hfad.model.OfferRequestModel

class UpdateOfferUseCaseImpl constructor(private val offerRepository: OfferRepository
):UpdateOfferUseCase {
    override suspend fun execute(
        userRequestId: Int,
        serviceProviderId: String,
        offer: OfferRequestModel
    ) {
        offerRepository.updateOffer( userRequestId, serviceProviderId, offer)
    }
}