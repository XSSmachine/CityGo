package com.example.domain.interfaces.offer_usecases

interface UpdateOfferStatusUseCase {
    suspend fun execute( sid: String, status: String)
}