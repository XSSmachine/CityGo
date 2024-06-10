package com.example.domain.usecases.offer

import com.example.domain.interfaces.OfferRepository
import com.example.domain.interfaces.offer_usecases.DeleteOfferUseCase

class DeleteOfferUseCaseImpl constructor(private val offerRepository: OfferRepository
):DeleteOfferUseCase {
    override suspend fun execute( sid: String) {
        offerRepository.deleteOffer( sid)
    }
}