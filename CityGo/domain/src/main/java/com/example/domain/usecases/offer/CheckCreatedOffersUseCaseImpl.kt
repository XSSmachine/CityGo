package com.example.domain.usecases.offer

import com.example.domain.interfaces.OfferRepository
import com.example.domain.interfaces.offer_usecases.CheckCreatedOffersUseCase
import com.hfad.model.RepoResult

class CheckCreatedOffersUseCaseImpl constructor(private val offerRepository: OfferRepository):
    CheckCreatedOffersUseCase {
    override suspend fun execute(sid: String, providerId:String): RepoResult<Boolean> {
        return offerRepository.checkCreatedOffers(sid,providerId)
    }
}