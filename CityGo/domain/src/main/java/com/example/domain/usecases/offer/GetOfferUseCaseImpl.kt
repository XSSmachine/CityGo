package com.example.domain.usecases.offer

import com.example.domain.interfaces.OfferRepository
import com.example.domain.interfaces.offer_usecases.GetOfferUseCase
import com.hfad.model.OfferResponseModel

class GetOfferUseCaseImpl constructor(private val offerRepository: OfferRepository
):GetOfferUseCase {
    override suspend fun execute(
        userRequestId: Int,
        serviceProviderId: String
    ): OfferResponseModel? {
        return offerRepository.getOffer( userRequestId, serviceProviderId)
    }
}