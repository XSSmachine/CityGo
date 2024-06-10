package com.example.domain.usecases.offer

import com.example.domain.interfaces.OfferRepository
import com.example.domain.interfaces.offer_usecases.UpdateOfferStatusUseCase

class UpdateOfferStatusUseCaseImpl constructor(private val offerRepository: OfferRepository
):UpdateOfferStatusUseCase {
    override suspend fun execute(
        sid: String,
        status: String
    ) {
        offerRepository.updateOfferStatus( sid, status)
    }
}