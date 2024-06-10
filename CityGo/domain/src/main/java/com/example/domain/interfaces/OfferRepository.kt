package com.example.domain.interfaces

import com.hfad.model.RepoResult
import com.hfad.model.OfferRequestModel
import com.hfad.model.OfferResponseModel

interface NullableOfferModel{
    val model : OfferResponseModel?
}

interface OfferRepository {
    suspend fun getAllOffers(userRequestUIID: String): RepoResult<List<OfferResponseModel>>
    suspend fun getAllMyOffers(serviceProviderId: String): RepoResult<List<OfferResponseModel>>
    suspend fun getOffer(sid: String): RepoResult<OfferResponseModel>
    suspend fun getOfferByUserRequestId(userRequestUIID: String): RepoResult<OfferResponseModel>
    suspend fun hasOffer(sid: String): RepoResult<Int>
    suspend fun deleteOffer(sid: String):RepoResult<Unit>
    suspend fun createOffer(offer: OfferRequestModel):RepoResult<Unit>
    suspend fun updateOfferStatus(sid: String, status: String): RepoResult<Unit>
    suspend fun updateOffer(sid: String, offer: OfferRequestModel): RepoResult<Unit>

    suspend fun checkCreatedOffers(userRequestUIID: String,providerId : String): RepoResult<Boolean>
}