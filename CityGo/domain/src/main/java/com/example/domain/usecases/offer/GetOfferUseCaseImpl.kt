package com.example.domain.usecases.offer

import com.example.domain.interfaces.OfferRepository
import com.example.domain.interfaces.offer_usecases.GetOfferUseCase
import com.hfad.model.BasicError
import com.hfad.model.Failure
import com.hfad.model.OfferResponseModel
import com.hfad.model.RepoResult
import com.hfad.model.Success
import com.hfad.model.onFailure
import com.hfad.model.onSuccess
import kotlinx.coroutines.flow.Flow

class GetOfferUseCaseImpl constructor(private val offerRepository: OfferRepository
):GetOfferUseCase {
    override suspend fun execute(
        sid: String,
    ): RepoResult<OfferResponseModel> {
        try {
            offerRepository.getOffer( sid)
            .onSuccess { return Success(it) }
                .onFailure { return Failure(it) }
        }catch (e: Exception) {
            e.printStackTrace()
            Failure(BasicError(Throwable("Get Offer Fetch failed")))
        }
        return Failure(BasicError(Throwable("Get Offer Fetch failed")))
    }
}