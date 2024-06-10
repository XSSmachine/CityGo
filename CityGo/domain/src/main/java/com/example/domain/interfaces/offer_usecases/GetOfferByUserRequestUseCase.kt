package com.example.domain.interfaces.offer_usecases

import com.hfad.model.OfferResponseModel
import com.hfad.model.RepoResult

interface GetOfferByUserRequestUseCase {
    suspend fun execute(userRequestUUID: String): RepoResult<OfferResponseModel>
}