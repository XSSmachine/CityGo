package com.example.domain.usecases.offer

import com.example.domain.interfaces.OfferRepository
import com.example.domain.interfaces.offer_usecases.HasOfferUseCase

class HasOfferUseCaseImpl constructor(private val offerRepository: OfferRepository):HasOfferUseCase {
    override suspend fun execute(userRequestId: Int, serviceProviderId: String): Int {
        return offerRepository.hasOffer(userRequestId,serviceProviderId)
    }
}