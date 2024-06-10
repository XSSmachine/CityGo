package com.example.domain.interfaces.offer_usecases

import com.hfad.model.OfferRequestModel

interface UpdateOfferUseCase {
    suspend fun execute(sid: String, offer: OfferRequestModel)
}