package com.example.domain.interfaces.offer_usecases

import com.hfad.model.OfferResponseModel

interface GetAllOffersUseCase {
    suspend fun execute(userRequestId:Int): List<OfferResponseModel>
}