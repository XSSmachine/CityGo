package com.example.domain.interfaces.offer_usecases

import com.hfad.model.RepoResult

interface CheckCreatedOffersUseCase {
    suspend fun execute(sid: String,providerId:String): RepoResult<Boolean>
}