package com.example.domain.interfaces.offer_usecases

import com.hfad.model.OfferResponseModel
import com.hfad.model.RepoResult
import kotlinx.coroutines.flow.Flow

interface GetOfferUseCase {
    suspend fun execute(sid: String): RepoResult<OfferResponseModel>
}