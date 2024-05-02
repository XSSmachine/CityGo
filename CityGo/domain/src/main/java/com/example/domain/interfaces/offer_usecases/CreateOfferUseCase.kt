package com.example.domain.interfaces.offer_usecases

import com.hfad.model.OfferRequestModel

interface CreateOfferUseCase {
    suspend fun execute(offer : OfferRequestModel)
}