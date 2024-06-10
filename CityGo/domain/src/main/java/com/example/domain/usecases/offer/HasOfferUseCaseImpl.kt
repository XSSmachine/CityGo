package com.example.domain.usecases.offer

import com.example.domain.interfaces.OfferRepository
import com.example.domain.interfaces.offer_usecases.HasOfferUseCase
import com.hfad.model.RepoResult

class HasOfferUseCaseImpl constructor(private val offerRepository: OfferRepository):HasOfferUseCase {
    override suspend fun execute(sid: String): RepoResult<Int> {
        return offerRepository.hasOffer(sid)
    }
}