package com.example.domain.interfaces.offer_usecases

interface DeleteOfferUseCase {
    suspend fun execute(sid: String)
}