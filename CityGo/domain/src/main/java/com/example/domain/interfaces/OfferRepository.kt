package com.example.domain.interfaces

import com.hfad.model.OfferRequestModel
import com.hfad.model.OfferResponseModel
import com.hfad.model.UserRequestRequestModel
import com.hfad.model.UserRequestResponseModel

interface OfferRepository {
    suspend fun getAllOffers(userRequestId: Int): List<OfferResponseModel>
    suspend fun getAllMyOffers(serviceProviderId: String): List<OfferResponseModel>
    suspend fun getOffer(userRequestId: Int,serviceProviderId: String,): OfferResponseModel?
    suspend fun hasOffer(userRequestId: Int,serviceProviderId: String,): Int
    suspend fun deleteOffer(userRequestId: Int,serviceProviderId: String,)
    suspend fun updateOffer(userRequestId: Int,serviceProviderId: String, offer: OfferRequestModel)
    suspend fun updateOfferStatus( userRequestId: Int,serviceProviderId: String, status: String)
    suspend fun createOffer(offer: OfferRequestModel)
}