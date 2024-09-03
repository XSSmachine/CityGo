package com.example.domain.usecases.offer

import android.util.Log
import com.example.domain.interfaces.OfferRepository
import com.example.domain.interfaces.offer_usecases.GetAllOffersUseCase
import com.hfad.model.BasicError
import com.hfad.model.Failure
import com.hfad.model.OfferResponseModel
import com.hfad.model.RepoResult
import com.hfad.model.Success
import com.hfad.model.onFailure
import com.hfad.model.onSuccess
import kotlinx.coroutines.flow.Flow

class GetAllOffersUseCaseImpl constructor(private val offerRepository: OfferRepository
):GetAllOffersUseCase {
    override suspend fun execute(userRequestUIID: String): RepoResult<List<OfferResponseModel>> {
        try {
            offerRepository.getAllOffers(userRequestUIID)
            .onSuccess { Log.d("userOffer-GetAll1",it.toString())
                return Success(it) }
            .onFailure {
                Log.d("userOffer-GetAll22",userRequestUIID.toString())
                Log.d("userOffer-GetAll2",it.toString())
                return Failure(it) }
        }catch (e: Exception) {
            e.printStackTrace()
            Failure(BasicError(Throwable("Get All Offers Fetch failed")))
        }
        return Failure(BasicError(Throwable("Get All Offers Fetch failed")))
    }
}