package com.example.domain.usecases.offer

import android.icu.lang.UCharacter.EastAsianWidth
import android.util.Log
import com.example.domain.interfaces.OfferRepository
import com.example.domain.interfaces.offer_usecases.GetAllMyOffersUseCase
import com.example.domain.interfaces.offer_usecases.GetAllOffersUseCase
import com.hfad.model.BasicError
import com.hfad.model.Failure
import com.hfad.model.OfferResponseModel
import com.hfad.model.RepoResult
import com.hfad.model.Success
import com.hfad.model.onFailure
import com.hfad.model.onSuccess
import kotlinx.coroutines.flow.Flow

class GetAllMyOffersUseCaseImpl constructor(private val offerRepository: OfferRepository
): GetAllMyOffersUseCase {
    override suspend fun execute(serviceProviderId: String): RepoResult<List<OfferResponseModel>> {
        try {
            offerRepository.getAllMyOffers(serviceProviderId)
                .onSuccess {
                    Log.d("userOffer-checkOffersUseCase",it.toString())
                    return Success(it) }
                .onFailure {
                    Log.d("userOffer-checkOffersUseCase",it.toString())
                    return Failure(it) }
        }catch (e: Exception) {
            e.printStackTrace()
            Failure(BasicError(Throwable("Get All My Offers Fetch failed")))
        }
        return Failure(BasicError(Throwable("Get All My Offers Fetch failed")))
    }
}